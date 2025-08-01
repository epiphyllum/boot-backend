package liquibase.snapshot;

import liquibase.CatalogAndSchema;
import liquibase.Scope;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.LiquibaseTableNamesFactory;
import liquibase.database.core.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.executor.jvm.ColumnMapRowMapper;
import liquibase.executor.jvm.RowMapperNotNullConstraintsResultSetExtractor;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;
import liquibase.util.JdbcUtil;
import liquibase.util.StringUtil;

import java.sql.*;
import java.util.*;

public class JdbcDatabaseSnapshot extends DatabaseSnapshot {

    private boolean warnedAboutDbaRecycleBin;
    private static final boolean ignoreWarnAboutDbaRecycleBin = Boolean.getBoolean("liquibase.ignoreRecycleBinWarning");

    private CachingDatabaseMetaData cachingDatabaseMetaData;

    private Map<String, CachedRow> cachedExpressionMap = null;

    private Set<String> userDefinedTypes;

    public JdbcDatabaseSnapshot(DatabaseObject[] examples, Database database, SnapshotControl snapshotControl) throws DatabaseException, InvalidExampleException {
        super(examples, database, snapshotControl);
    }

    public JdbcDatabaseSnapshot(DatabaseObject[] examples, Database database) throws DatabaseException, InvalidExampleException {
        super(examples, database);
    }

    public CachingDatabaseMetaData getMetaDataFromCache() throws SQLException {
        if (cachingDatabaseMetaData == null) {
            DatabaseMetaData databaseMetaData = null;
            if (getDatabase().getConnection() != null) {
                databaseMetaData = ((JdbcConnection) getDatabase().getConnection()).getUnderlyingConnection().getMetaData();
            }

            cachingDatabaseMetaData = new CachingDatabaseMetaData(this.getDatabase(), databaseMetaData);
        }
        return cachingDatabaseMetaData;
    }

    public class CachingDatabaseMetaData {
        private static final String SQL_FILTER_MATCH_ALL = "%";
        private final DatabaseMetaData databaseMetaData;
        private final Database database;

        public CachingDatabaseMetaData(Database database, DatabaseMetaData metaData) {
            this.databaseMetaData = metaData;
            this.database = database;
        }

        public DatabaseMetaData getDatabaseMetaData() {
            return databaseMetaData;
        }

        public List<CachedRow> getForeignKeys(final String catalogName, final String schemaName, final String tableName,
                                              final String fkName) throws DatabaseException {
            ForeignKeysResultSetCache foreignKeysResultSetCache = new ForeignKeysResultSetCache(database, catalogName, schemaName, tableName, fkName);
            ResultSetCache importedKeys = getResultSetCache("getImportedKeys");
            importedKeys.setBulkTracking(!(database instanceof MSSQLDatabase));

            return importedKeys.get(foreignKeysResultSetCache);
        }

        public List<CachedRow> getIndexInfo(final String catalogName, final String schemaName, final String tableName, final String indexName) throws DatabaseException, SQLException {

            return getResultSetCache("getIndexInfo").get(new ResultSetCache.UnionResultSetExtractor(database) {

                public boolean isBulkFetchMode;

                @Override
                public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                    return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"), database, row.getString("TABLE_NAME"), row.getString("INDEX_NAME"));
                }

                @Override
                public ResultSetCache.RowData wantedKeyParameters() {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, tableName, indexName);
                }

                @Override
                public boolean bulkContainsSchema(String schemaKey) {
                    return getAllCatalogsStringScratchData() != null && database instanceof OracleDatabase;
                }

                @Override
                public String getSchemaKey(CachedRow row) {
                    return row.getString("TABLE_SCHEM");
                }

                @Override
                public List<CachedRow> fastFetch() throws SQLException, DatabaseException {
                    List<CachedRow> returnList = new ArrayList<>();

                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    if (database instanceof OracleDatabase) {
                        warnAboutDbaRecycleBin();

                        //oracle getIndexInfo is buggy and slow.  See Issue 1824548 and http://forums.oracle.com/forums/thread.jspa?messageID=578383&#578383
                        String sql =
                                "SELECT " +
                                        "c.INDEX_NAME, " +
                                        "3 AS TYPE, " +
                                        "c.TABLE_OWNER AS TABLE_SCHEM, " +
                                        "c.TABLE_NAME, " +
                                        "c.COLUMN_NAME, " +
                                        "c.COLUMN_POSITION AS ORDINAL_POSITION, " +
                                        "NULL AS FILTER_CONDITION, " +
                                        "c.INDEX_OWNER, " +
                                        "CASE I.UNIQUENESS WHEN 'UNIQUE' THEN 0 ELSE 1 END AS NON_UNIQUE, " +
                                        "CASE c.DESCEND WHEN 'Y' THEN 'D' WHEN 'DESC' THEN 'D' WHEN 'N' THEN 'A' WHEN 'ASC' THEN 'A' END AS ASC_OR_DESC, " +
                                        "CASE WHEN tablespace_name = (SELECT default_tablespace FROM user_users) " +
                                        "THEN NULL ELSE tablespace_name END AS tablespace_name  " +
                                        "FROM ALL_IND_COLUMNS c " +
                                        "JOIN ALL_INDEXES i ON i.owner=c.index_owner AND i.index_name = c.index_name and i.table_owner = c.table_owner " +
                                        "LEFT OUTER JOIN " + (((OracleDatabase) database).canAccessDbaRecycleBin() ? "dba_recyclebin" : "user_recyclebin") + " d ON d.object_name=c.table_name ";
                        if (!isBulkFetchMode || getAllCatalogsStringScratchData() == null) {
                            sql += "WHERE c.TABLE_OWNER = '" + database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class) + "' ";
                        } else {
                            sql += "WHERE c.TABLE_OWNER IN ('" + database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class) + "', " + getAllCatalogsStringScratchData() + ")";
                        }
                        sql += "AND i.OWNER = c.TABLE_OWNER " +
                                "AND d.object_name IS NULL ";


                        if (!isBulkFetchMode && (tableName != null)) {
                            sql += " AND c.TABLE_NAME='" + tableName + "'";
                        }

                        if (!isBulkFetchMode && (indexName != null)) {
                            sql += " AND c.INDEX_NAME='" + indexName + "'";
                        }

                        sql += " ORDER BY c.INDEX_NAME, ORDINAL_POSITION";

                        returnList.addAll(setIndexExpressions(executeAndExtract(sql, database)));
                    } else if (database instanceof MSSQLDatabase) {
                        String tableCat = "original_db_name()";

                        if (9 <= database.getDatabaseMajorVersion()) {
                            tableCat = "db_name()";
                        }
                        //fetch additional index info
                        String sql = "SELECT " +
                                tableCat + " as TABLE_CAT, " +
                                "object_schema_name(i.object_id) as TABLE_SCHEM, " +
                                "object_name(i.object_id) as TABLE_NAME, " +
                                "CASE is_unique WHEN 1 then 0 else 1 end as NON_UNIQUE, " +
                                "object_name(i.object_id) as INDEX_QUALIFIER, " +
                                "i.name as INDEX_NAME, " +
                                "case i.type when 1 then 1 ELSE 3 end as TYPE, " +
                                "key_ordinal as ORDINAL_POSITION, " +
                                "COL_NAME(c.object_id,c.column_id) AS COLUMN_NAME, " +
                                "case is_descending_key when 0 then 'A' else 'D' end as ASC_OR_DESC, " +
                                "null as CARDINALITY, " +
                                "null as PAGES, " +
                                "i.filter_definition as FILTER_CONDITION, " +
                                "o.type AS INTERNAL_OBJECT_TYPE, " +
                                "i.*, " +
                                "c.*, " +
                                "s.* " +
                                "FROM sys.indexes i " +
                                "join sys.index_columns c on i.object_id=c.object_id and i.index_id=c.index_id " +
                                "join sys.stats s on i.object_id=s.object_id and i.name=s.name " +
                                "join sys.objects o on i.object_id=o.object_id " +
                                "WHERE object_schema_name(i.object_id)='" + database.correctObjectName(catalogAndSchema.getSchemaName(), Schema.class) + "'";

                        if (!isBulkFetchMode && (tableName != null)) {
                            sql += " AND object_name(i.object_id)='" + database.escapeStringForDatabase(tableName) + "'";
                        }

                        if (!isBulkFetchMode && (indexName != null)) {
                            sql += " AND i.name='" + database.escapeStringForDatabase(indexName) + "'";
                        }

                        sql += "ORDER BY i.object_id, i.index_id, c.key_ordinal";

                        returnList.addAll(executeAndExtract(sql, database));

                    } else if (database instanceof Db2zDatabase) {
                        List<String> parameters = new ArrayList<>(3);
                        String sql = "SELECT i.CREATOR AS TABLE_SCHEM, " +
                                "i.TBNAME AS TABLE_NAME, " +
                                "i.NAME AS INDEX_NAME, " +
                                "3 AS TYPE, " +
                                "k.COLNAME AS COLUMN_NAME, " +
                                "k.COLSEQ AS ORDINAL_POSITION, " +
                                "CASE UNIQUERULE WHEN 'D' then 1 else 0 end as NON_UNIQUE, " +
                                "k.ORDERING AS ORDER, " +
                                "i.CREATOR AS INDEX_QUALIFIER " +
                                "FROM SYSIBM.SYSKEYS k " +
                                "JOIN SYSIBM.SYSINDEXES i " +
                                "ON k.IXNAME = i.NAME " +
                                "AND k.IXCREATOR = i.CREATOR " +
                                "WHERE  i.CREATOR = ?";
                        parameters.add(database.correctObjectName(catalogAndSchema.getSchemaName(), Schema.class));
                        if (!isBulkFetchMode && tableName != null) {
                            sql += " AND i.TBNAME = ?";
                            parameters.add(database.escapeStringForDatabase(tableName));
                        }

                        if (!isBulkFetchMode && indexName != null) {
                            sql += " AND i.NAME = ?";
                            parameters.add(database.escapeStringForDatabase(indexName));
                        }

                        sql += "ORDER BY i.NAME, k.COLSEQ";

                        returnList.addAll(executeAndExtract(database, sql, parameters.toArray()));
                    } else if (!(database instanceof MariaDBDatabase) && database instanceof MySQLDatabase) {

                        //mysql 8.0.13 introduced support for indexes on `lower(first_name)` which comes back in an "expression" column
                        String filterConditionValue = "NULL";
                        if (database.getDatabaseMajorVersion() > 8 || (database.getDatabaseMajorVersion() == 8 && ((MySQLDatabase) database).getDatabasePatchVersion() >= 13)) {
                            filterConditionValue = "EXPRESSION";
                        }

                        StringBuilder sql = new StringBuilder("SELECT TABLE_CATALOG AS TABLE_CAT, TABLE_SCHEMA AS TABLE_SCHEM,");
                        sql.append(" TABLE_NAME, NON_UNIQUE, NULL AS INDEX_QUALIFIER, INDEX_NAME,");
                        sql.append(DatabaseMetaData.tableIndexOther);
                        sql.append(" AS TYPE, SEQ_IN_INDEX AS ORDINAL_POSITION, COLUMN_NAME,");
                        sql.append("COLLATION AS ASC_OR_DESC, CARDINALITY, 0 AS PAGES, " + filterConditionValue + " AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE");
                        sql.append(" TABLE_SCHEMA = '").append(database.correctObjectName(catalogAndSchema.getCatalogName(), Catalog.class)).append("'");

                        if (!isBulkFetchMode && tableName != null) {
                            sql.append(" AND TABLE_NAME = '").append(database.escapeStringForDatabase(tableName)).append("'");
                        }

                        if (!isBulkFetchMode && indexName != null) {
                            sql.append(" AND INDEX_NAME='").append(database.escapeStringForDatabase(indexName)).append("'");
                        }

                        sql.append("ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX");

                        returnList.addAll(executeAndExtract(sql.toString(), database));
                    } else {
                        /*
                         * If we do not know in which table to look for the index, things get a little bit ugly.
                         * First, we get a collection of all tables within the catalogAndSchema, then iterate through
                         * them until we (hopefully) find the index we are looking for.
                         */
                        List<String> tables = new ArrayList<>();
                        if (tableName == null) {
                            // Build a list of all candidate tables in the catalog/schema that might contain the index
                            for (CachedRow row : getTables(((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema), ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema), null)) {
                                tables.add(row.getString("TABLE_NAME"));
                            }
                        } else {
                            tables.add(tableName);
                        }

                        // Iterate through all the candidate tables and try to find the index.
                        for (String tableName1 : tables) {
                            ResultSet rs = databaseMetaData.getIndexInfo(
                                    ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema),
                                    ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema),
                                    tableName1,
                                    false,
                                    true);
                            List<CachedRow> rows = extract(rs, (database instanceof InformixDatabase));
                            returnList.addAll(rows);
                        }
                    }

                    return returnList;
                }

                private List<CachedRow> setIndexExpressions(List<CachedRow> c) throws DatabaseException, SQLException {
                    Map<String, CachedRow> expressionMap = getCachedExpressionMap();
                    c.forEach(row -> {
                        row.set("FILTER_CONDITION", null);
                        String key = row.getString("INDEX_OWNER") + "::" + row.getString("INDEX_NAME") + "::" +
                                row.getInt("ORDINAL_POSITION");
                        CachedRow fromMap = expressionMap.get(key);
                        if (fromMap != null) {
                            row.set("FILTER_CONDITION", fromMap.get("COLUMN_EXPRESSION"));
                        }
                    });
                    return c;
                }

                private Map<String, CachedRow> getCachedExpressionMap() throws DatabaseException, SQLException {
                    if (cachedExpressionMap != null) {
                        return cachedExpressionMap;
                    }
                    String expSql = "SELECT e.column_expression, e.index_owner, e.index_name, e.column_position FROM all_ind_expressions e";
                    List<CachedRow> ec = executeAndExtract(expSql, database);
                    cachedExpressionMap = new HashMap<>();
                    ec.forEach(row -> {
                        String key = row.getString("INDEX_OWNER") + "::" + row.getString("INDEX_NAME") + "::" +
                                row.getInt("COLUMN_POSITION");
                        cachedExpressionMap.put(key, row);
                    });
                    return cachedExpressionMap;
                }

                @Override
                public List<CachedRow> bulkFetch() throws SQLException, DatabaseException {
                    this.isBulkFetchMode = true;
                    return fastFetch();
                }

                @Override
                protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                    if (database instanceof OracleDatabase || database instanceof MSSQLDatabase) {
                        return JdbcDatabaseSnapshot.this.getAllCatalogsStringScratchData() != null || (tableName == null && indexName == null) || super.shouldBulkSelect(schemaKey, resultSetCache);
                    }
                    return false;
                }
            });
        }


        protected void warnAboutDbaRecycleBin() {
            if (!ignoreWarnAboutDbaRecycleBin && !warnedAboutDbaRecycleBin && !(((OracleDatabase) database).canAccessDbaRecycleBin())) {
                Scope.getCurrentScope().getLog(getClass()).warning(((OracleDatabase) database).getDbaRecycleBinWarning());
                warnedAboutDbaRecycleBin = true;
            }
        }

        /**
         * Return the columns for the given catalog, schema, table, and column.
         */
        public List<CachedRow> getColumns(final String catalogName, final String schemaName, final String tableName, final String columnName) throws SQLException, DatabaseException {

            if ((database instanceof MSSQLDatabase) && (userDefinedTypes == null)) {
                userDefinedTypes = new HashSet<>();
                DatabaseConnection databaseConnection = database.getConnection();
                if (databaseConnection instanceof JdbcConnection) {
                    Statement stmt = null;
                    ResultSet resultSet = null;
                    try {
                        stmt = ((JdbcConnection) databaseConnection).getUnderlyingConnection().createStatement();
                        resultSet = stmt.executeQuery("select name from " + (catalogName == null ? "" : "[" + catalogName + "].") + "sys.types where is_user_defined=1");
                        while (resultSet.next()) {
                            userDefinedTypes.add(resultSet.getString("name").toLowerCase());
                        }
                    } finally {
                        JdbcUtil.close(resultSet, stmt);
                    }
                }
            }
            GetColumnResultSetCache getColumnResultSetCache = new GetColumnResultSetCache(database, catalogName,
                    schemaName, tableName, columnName);
            return getResultSetCache("getColumns").get(getColumnResultSetCache);
        }

        /**
         * Return the NotNullConstraints for the given catalog, schema, table, and column.
         */
        public List<CachedRow> getNotNullConst(final String catalogName, final String schemaName,
                                               final String tableName) throws DatabaseException {
            if (!(database instanceof OracleDatabase)) {
                return Collections.emptyList();
            }
            GetNotNullConstraintsResultSetCache getNotNullConstraintsResultSetCache = new GetNotNullConstraintsResultSetCache(database, catalogName,
                    schemaName, tableName);
            return getResultSetCache("getNotNullConst").get(getNotNullConstraintsResultSetCache);
        }

        private class GetColumnResultSetCache extends ResultSetCache.SingleResultSetExtractor {
            final String catalogName;
            final String schemaName;
            final String tableName;
            final String columnName;

            private GetColumnResultSetCache(Database database, String catalogName, String schemaName, String tableName, String columnName) {
                super(database);
                this.catalogName = catalogName;
                this.schemaName = schemaName;
                this.tableName = tableName;
                this.columnName = columnName;
            }

            @Override
            public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"), database, row.getString("TABLE_NAME"), row.getString("COLUMN_NAME"));
            }

            @Override
            public ResultSetCache.RowData wantedKeyParameters() {
                return new ResultSetCache.RowData(catalogName, schemaName, database, tableName, columnName);
            }

            @Override
            public boolean bulkContainsSchema(String schemaKey) {
                String catalogs = getAllCatalogsStringScratchData();
                return catalogs != null && schemaKey != null
                        && catalogs.contains("'" + schemaKey.toUpperCase() + "'")
                        && database instanceof OracleDatabase;
            }

            @Override
            public String getSchemaKey(CachedRow row) {
                return row.getString("TABLE_SCHEM");
            }

            @Override
            protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                LiquibaseTableNamesFactory liquibaseTableNamesFactory = Scope.getCurrentScope().getSingleton(LiquibaseTableNamesFactory.class);
                List<String> liquibaseTableNames = liquibaseTableNamesFactory.getLiquibaseTableNames(database);
                return liquibaseTableNames.stream().noneMatch(tableName::equalsIgnoreCase);
            }

            @Override
            public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
                if (database instanceof OracleDatabase) {
                    return oracleQuery(false);
                } else if (database instanceof MSSQLDatabase) {
                    return mssqlQuery(false);
                }
                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                try {
                    List<CachedRow> returnList =
                            extract(
                                    databaseMetaData.getColumns(
                                            ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema),
                                            escapeForLike(((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema), database),
                                            escapeForLike(tableName, database),
                                            SQL_FILTER_MATCH_ALL)
                            );
                    //
                    // IF MARIADB OR SQL ANYWHERE
                    // Query to get actual data types and then map each column to its CachedRow
                    //
                    determineActualDataTypes(returnList, tableName);
                    return returnList;
                } catch (SQLException e) {
                    if (shouldReturnEmptyColumns(e)) { //view with table already dropped. Act like it has no columns.
                        return new ArrayList<>();
                    } else {
                        throw e;
                    }
                }
            }

            @Override
            public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
                if (database instanceof OracleDatabase) {
                    return oracleQuery(true);
                } else if (database instanceof MSSQLDatabase) {
                    return mssqlQuery(true);
                }

                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                try {
                    List<CachedRow> returnList =
                            extract(databaseMetaData.getColumns(((AbstractJdbcDatabase) database)
                                            .getJdbcCatalogName(catalogAndSchema),
                                    escapeForLike(((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema), database),
                                    SQL_FILTER_MATCH_ALL, SQL_FILTER_MATCH_ALL));
                    //
                    // IF MARIADB OR SQL ANYWHERE
                    // Query to get actual data types and then map each column to its CachedRow
                    //
                    determineActualDataTypes(returnList, null);
                    return returnList;
                } catch (SQLException e) {
                    if (shouldReturnEmptyColumns(e)) {
                        return new ArrayList<>();
                    } else {
                        throw e;
                    }
                }
            }

            //
            // For MariaDB, query for the data type column so that we can correctly
            // set the DATETIME(6) type if specified
            //
            // For SQL Anywhere, query for the scale column so we can correctly
            // set the size unit
            //
            private void determineActualDataTypes(List<CachedRow> returnList, String tableName) throws SQLException {
                //
                // If not MariaDB / SQL Anywhere then just return
                //
                if (!(database instanceof MariaDBDatabase || database instanceof SybaseASADatabase)) {
                    return;
                }

                if (database instanceof SybaseASADatabase) {
                    //
                    // Query for actual data type for column. The actual SYSTABCOL.scale column value is
                    // not reported by the DatabaseMetadata.getColumns() query for CHAR-limited (in contrast
                    // to BYTE-limited) columns, and it is needed to capture the kind if limitation.
                    // The actual SYSTABCOL.column_type is not reported by the DatabaseMetadata.getColumns()
                    // query as the IS_GENERATEDCOLUMN columns is missing in the result set, and it is needed to
                    // capture the kind of column (regular or computed).
                    //
                    // See https://help.sap.com/docs/SAP_SQL_Anywhere/93079d4ba8e44920ae63ffb4def91f5b/3beaa3956c5f1014883cb0c3e3559cc9.html.
                    //
                    String selectStatement =
                            "SELECT table_name, column_name, scale, column_type FROM SYSTABCOL KEY JOIN SYSTAB KEY JOIN SYSUSER " +
                                    "WHERE user_name = ? AND ? IS NULL OR table_name = ?";
                    Connection underlyingConnection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
                    try (PreparedStatement stmt = underlyingConnection.prepareStatement(selectStatement)) {
                        stmt.setString(1, schemaName);
                        stmt.setString(2, tableName);
                        stmt.setString(3, tableName);
                        try (ResultSet columnSelectRS = stmt.executeQuery()) {
                            while (columnSelectRS.next()) {
                                String selectedTableName = columnSelectRS.getString("table_name");
                                String selectedColumnName = columnSelectRS.getString("column_name");
                                int selectedScale = columnSelectRS.getInt("scale");
                                String selectedColumnType = columnSelectRS.getString("column_type");
                                for (CachedRow row : returnList) {
                                    String rowTableName = row.getString("TABLE_NAME");
                                    String rowColumnName = row.getString("COLUMN_NAME");
                                    if (rowTableName.equalsIgnoreCase(selectedTableName) &&
                                            rowColumnName.equalsIgnoreCase(selectedColumnName)) {
                                        int rowDataType = row.getInt("DATA_TYPE");
                                        if (rowDataType == Types.VARCHAR || rowDataType == Types.CHAR) {
                                            row.set("scale", selectedScale);
                                        }
                                        row.set("IS_GENERATEDCOLUMN", "C".equals(selectedColumnType) ? "YES" : "NO");
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (SQLException sqle) {
                        throw new RuntimeException(sqle);
                        //
                        // Do not stop
                        //
                    }
                    return;
                }

                //
                // Query for actual data type for column. The actual DATA_TYPE column string is
                // not returned by the DatabaseMetadata.getColumns() query, and it is needed
                // to capture DATETIME(<precision>) data types.
                //
                StringBuilder selectStatement = new StringBuilder(
                        "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ?");
                if (tableName != null) {
                    selectStatement.append(" AND TABLE_NAME = ?");
                }
                Connection underlyingConnection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
                PreparedStatement statement = underlyingConnection.prepareStatement(selectStatement.toString());
                statement.setString(1, schemaName);
                if (tableName != null) {
                    statement.setString(2, tableName);
                }
                try {
                    ResultSet columnSelectRS = statement.executeQuery(selectStatement.toString());
                    //
                    // Iterate the result set from the query and match the rows
                    // to the rows that were returned by getColumns() in order
                    // to assign the actual DATA_TYPE string to the appropriate row.
                    //
                    while (columnSelectRS.next()) {
                        String selectedTableName = columnSelectRS.getString("TABLE_NAME");
                        String selectedColumnName = columnSelectRS.getString("COLUMN_NAME");
                        String actualDataType = columnSelectRS.getString("DATA_TYPE");
                        for (CachedRow row : returnList) {
                            String rowTableName = row.getString("TABLE_NAME");
                            String rowColumnName = row.getString("COLUMN_NAME");
                            String rowTypeName = row.getString("TYPE_NAME");
                            int rowDataType = row.getInt("DATA_TYPE");
                            if (rowTableName.equalsIgnoreCase(selectedTableName) &&
                                    rowColumnName.equalsIgnoreCase(selectedColumnName) &&
                                    rowTypeName.equalsIgnoreCase("datetime") &&
                                    rowDataType == Types.OTHER &&
                                    !rowTypeName.equalsIgnoreCase(actualDataType)) {
                                row.set("TYPE_NAME", actualDataType);
                                row.set("DATA_TYPE", Types.TIMESTAMP);
                                break;
                            }
                        }
                    }
                } catch (SQLException sqle) {
                    //
                    // Do not stop
                    //
                } finally {
                    JdbcUtil.closeStatement(statement);
                }
            }

            protected boolean shouldReturnEmptyColumns(SQLException e) {
                return e.getMessage().contains("references invalid table"); //view with table already dropped. Act like it has no columns.
            }

            protected List<CachedRow> oracleQuery(boolean bulk) throws DatabaseException, SQLException {
                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                boolean collectIdentityData = database.getDatabaseMajorVersion() >= OracleDatabase.ORACLE_12C_MAJOR_VERSION;

                String sql = "select NULL AS TABLE_CAT, OWNER AS TABLE_SCHEM, 'NO' as IS_AUTOINCREMENT, cc.COMMENTS AS REMARKS," +
                        "OWNER, TABLE_NAME, COLUMN_NAME, DATA_TYPE AS DATA_TYPE_NAME, DATA_TYPE_MOD, DATA_TYPE_OWNER, " +
                        // note: oracle reports DATA_LENGTH=4*CHAR_LENGTH when using VARCHAR( <N> CHAR ), thus BYTEs
                        "DECODE (c.data_type, 'CHAR', 1, 'VARCHAR2', 12, 'NUMBER', 3, 'LONG', -1, 'DATE', " + "93" + ", 'RAW', -3, 'LONG RAW', -4, 'BLOB', 2004, 'CLOB', 2005, 'BFILE', -13, 'FLOAT', 6, 'TIMESTAMP(6)', 93, 'TIMESTAMP(6) WITH TIME ZONE', -101, 'TIMESTAMP(6) WITH LOCAL TIME ZONE', -102, 'INTERVAL YEAR(2) TO MONTH', -103, 'INTERVAL DAY(2) TO SECOND(6)', -104, 'BINARY_FLOAT', 100, 'BINARY_DOUBLE', 101, 'XMLTYPE', 2009, 1111) AS data_type, " +
                        "DECODE( CHAR_USED, 'C',CHAR_LENGTH, DATA_LENGTH ) as DATA_LENGTH, " +
                        "DATA_PRECISION, DATA_SCALE, NULLABLE, COLUMN_ID as ORDINAL_POSITION, DEFAULT_LENGTH, " +
                        "DATA_DEFAULT, " +
                        "NUM_BUCKETS, CHARACTER_SET_NAME, " +
                        "CHAR_COL_DECL_LENGTH, CHAR_LENGTH, " +
                        "CHAR_USED, VIRTUAL_COLUMN ";
                if (collectIdentityData) {
                    sql += ", DEFAULT_ON_NULL, IDENTITY_COLUMN, ic.GENERATION_TYPE ";
                }
                sql += "FROM ALL_TAB_COLS c " +
                        "JOIN ALL_COL_COMMENTS cc USING ( OWNER, TABLE_NAME, COLUMN_NAME ) ";
                if (collectIdentityData) {
                    sql += "LEFT JOIN ALL_TAB_IDENTITY_COLS ic USING (OWNER, TABLE_NAME, COLUMN_NAME ) ";
                }
                if (!bulk || getAllCatalogsStringScratchData() == null) {
                    sql += "WHERE OWNER='" + jdbcSchemaName + "' AND hidden_column='NO'";
                } else {
                    sql += "WHERE OWNER IN ('" + jdbcSchemaName + "', " + getAllCatalogsStringScratchData() + ") AND hidden_column='NO'";
                }

                if (!bulk) {
                    if (tableName != null) {
                        sql += " AND TABLE_NAME='" + database.escapeStringForDatabase(tableName) + "'";
                    }
                    if (columnName != null) {
                        sql += " AND COLUMN_NAME='" + database.escapeStringForDatabase(columnName) + "'";
                    }
                }
                sql += " AND " + ((OracleDatabase) database).getSystemTableWhereClause("TABLE_NAME");
                sql += " ORDER BY OWNER, TABLE_NAME, c.COLUMN_ID";

                return this.executeAndExtract(sql, database);
            }


            protected List<CachedRow> mssqlQuery(boolean bulk) throws DatabaseException, SQLException {
                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                String databaseName = StringUtil.trimToNull(database.correctObjectName(catalogAndSchema.getCatalogName(), Catalog.class));
                String dbIdParam;
                String databasePrefix;
                if (databaseName == null) {
                    databasePrefix = "";
                    dbIdParam = "";
                } else {
                    dbIdParam = ", db_id('" + databaseName + "')";
                    databasePrefix = "[" + databaseName + "].";
                }

                String sql = "select " +
                        "db_name(" + (databaseName == null ? "" : "db_id('" + databaseName + "')") + ") AS TABLE_CAT, " +
                        "object_schema_name(c.object_id" + dbIdParam + ") AS TABLE_SCHEM, " +
                        "object_name(c.object_id" + dbIdParam + ") AS TABLE_NAME, " +
                        "c.name AS COLUMN_NAME, " +
                        "is_filestream AS IS_FILESTREAM, " +
                        "is_rowguidcol AS IS_ROWGUIDCOL, " +
                        "CASE WHEN c.is_identity = 'true' THEN 'YES' ELSE 'NO' END as IS_AUTOINCREMENT, " +
                        "{REMARKS_COLUMN_PLACEHOLDER}" +
                        "t.name AS TYPE_NAME, " +
                        "dc.name as COLUMN_DEF_NAME, " +
                        "dc.definition as COLUMN_DEF, " +
                        // data type mapping from https://msdn.microsoft.com/en-us/library/ms378878(v=sql.110).aspx
                        "CASE t.name " +
                        "WHEN 'bigint' THEN " + Types.BIGINT + " " +
                        "WHEN 'binary' THEN " + Types.BINARY + " " +
                        "WHEN 'bit' THEN " + Types.BIT + " " +
                        "WHEN 'char' THEN " + Types.CHAR + " " +
                        "WHEN 'date' THEN " + Types.DATE + " " +
                        "WHEN 'datetime' THEN " + Types.TIMESTAMP + " " +
                        "WHEN 'datetime2' THEN " + Types.TIMESTAMP + " " +
                        "WHEN 'datetimeoffset' THEN -155 " +
                        "WHEN 'decimal' THEN " + Types.DECIMAL + " " +
                        "WHEN 'float' THEN " + Types.DOUBLE + " " +
                        "WHEN 'image' THEN " + Types.LONGVARBINARY + " " +
                        "WHEN 'int' THEN " + Types.INTEGER + " " +
                        "WHEN 'money' THEN " + Types.DECIMAL + " " +
                        "WHEN 'nchar' THEN " + Types.NCHAR + " " +
                        "WHEN 'ntext' THEN " + Types.LONGNVARCHAR + " " +
                        "WHEN 'numeric' THEN " + Types.NUMERIC + " " +
                        "WHEN 'nvarchar' THEN " + Types.NVARCHAR + " " +
                        "WHEN 'real' THEN " + Types.REAL + " " +
                        "WHEN 'smalldatetime' THEN " + Types.TIMESTAMP + " " +
                        "WHEN 'smallint' THEN " + Types.SMALLINT + " " +
                        "WHEN 'smallmoney' THEN " + Types.DECIMAL + " " +
                        "WHEN 'text' THEN " + Types.LONGVARCHAR + " " +
                        "WHEN 'time' THEN " + Types.TIME + " " +
                        "WHEN 'timestamp' THEN " + Types.BINARY + " " +
                        "WHEN 'tinyint' THEN " + Types.TINYINT + " " +
                        "WHEN 'udt' THEN " + Types.VARBINARY + " " +
                        "WHEN 'uniqueidentifier' THEN " + Types.CHAR + " " +
                        "WHEN 'varbinary' THEN " + Types.VARBINARY + " " +
                        "WHEN 'varbinary(max)' THEN " + Types.VARBINARY + " " +
                        "WHEN 'varchar' THEN " + Types.VARCHAR + " " +
                        "WHEN 'varchar(max)' THEN " + Types.VARCHAR + " " +
                        "WHEN 'xml' THEN " + Types.LONGVARCHAR + " " +
                        "WHEN 'LONGNVARCHAR' THEN " + Types.SQLXML + " " +
                        "ELSE " + Types.OTHER + " END AS DATA_TYPE, " +
                        "CASE WHEN c.is_nullable = 'true' THEN 1 ELSE 0 END AS NULLABLE, " +
                        "10 as NUM_PREC_RADIX, " +
                        "c.column_id as ORDINAL_POSITION, " +
                        "c.scale as DECIMAL_DIGITS, " +
                        "c.max_length as COLUMN_SIZE, " +
                        "c.precision as DATA_PRECISION, " +
                        "c.is_computed as IS_COMPUTED " +
                        "FROM " + databasePrefix + "sys.columns c " +
                        "inner join " + databasePrefix + "sys.types t on c.user_type_id=t.user_type_id " +
                        "{REMARKS_JOIN_PLACEHOLDER}" +
                        "left outer join " + databasePrefix + "sys.default_constraints dc on dc.parent_column_id = c.column_id AND dc.parent_object_id=c.object_id AND type_desc='DEFAULT_CONSTRAINT' " +
                        "WHERE object_schema_name(c.object_id" + dbIdParam + ")='" + ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema) + "'";


                if (!bulk) {
                    if (tableName != null) {
                        sql += " and object_name(c.object_id" + dbIdParam + ")='" + database.escapeStringForDatabase(tableName) + "'";
                    }
                    if (columnName != null) {
                        sql += " and c.name='" + database.escapeStringForDatabase(columnName) + "'";
                    }
                }
                sql += "order by object_schema_name(c.object_id" + dbIdParam + "), object_name(c.object_id" + dbIdParam + "), c.column_id";


                // sys.extended_properties is added to Azure on V12: https://feedback.azure.com/forums/217321-sql-database/suggestions/6549815-add-sys-extended-properties-for-meta-data-support
                if ((!((MSSQLDatabase) database).isAzureDb()) // Either NOT AzureDB (=SQL Server 2008 or higher)
                        || (database.getDatabaseMajorVersion() >= 12)) { // or at least AzureDB v12
                    // SQL Server 2005 or later
                    // https://technet.microsoft.com/en-us/library/ms177541.aspx
                    sql = sql.replace("{REMARKS_COLUMN_PLACEHOLDER}", "CAST([ep].[value] AS [nvarchar](MAX)) AS [REMARKS], ");
                    sql = sql.replace("{REMARKS_JOIN_PLACEHOLDER}", "left outer join " + databasePrefix + "[sys].[extended_properties] AS [ep] ON [ep].[class] = 1 " +
                            "AND [ep].[major_id] = c.object_id " +
                            "AND [ep].[minor_id] = column_id " +
                            "AND [ep].[name] = 'MS_Description' ");
                } else {
                    sql = sql.replace("{REMARKS_COLUMN_PLACEHOLDER}", "");
                    sql = sql.replace("{REMARKS_JOIN_PLACEHOLDER}", "");
                }

                List<CachedRow> rows = this.executeAndExtract(sql, database);

                for (CachedRow row : rows) {
                    String typeName = row.getString("TYPE_NAME");
                    if ("nvarchar".equals(typeName) || "nchar".equals(typeName)) {
                        Integer size = row.getInt("COLUMN_SIZE");
                        if (size > 0) {
                            row.set("COLUMN_SIZE", size / 2);
                        }
                    } else if ((row.getInt("DATA_PRECISION") != null) && (row.getInt("DATA_PRECISION") > 0)) {
                        row.set("COLUMN_SIZE", row.getInt("DATA_PRECISION"));
                    }
                }

                return rows;
            }

            @Override
            protected List<CachedRow> extract(ResultSet resultSet, boolean informixIndexTrimHint) throws SQLException {
                List<CachedRow> rows = super.extract(resultSet, informixIndexTrimHint);
                if ((database instanceof MSSQLDatabase) && !userDefinedTypes.isEmpty()) { //UDT types in MSSQL don't take parameters
                    for (CachedRow row : rows) {
                        String dataType = (String) row.get("TYPE_NAME");
                        if (userDefinedTypes.contains(dataType.toLowerCase())) {
                            row.set("COLUMN_SIZE", null);
                            row.set("DECIMAL_DIGITS ", null);
                        }
                    }
                }
                return rows;
            }
        }

        private class ForeignKeysResultSetCache extends ResultSetCache.UnionResultSetExtractor {
            final String catalogName;
            final String schemaName;
            final String tableName;
            final String fkName;

            private ForeignKeysResultSetCache(Database database, String catalogName, String schemaName, String tableName, String fkName) {
                super(database);
                this.catalogName = catalogName;
                this.schemaName = schemaName;
                this.tableName = tableName;
                this.fkName = fkName;
            }

            @Override
            public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                return new ResultSetCache.RowData(row.getString("FKTABLE_CAT"), row.getString("FKTABLE_SCHEM"), database, row.getString("FKTABLE_NAME"), row.getString("FK_NAME"));
            }

            @Override
            public ResultSetCache.RowData wantedKeyParameters() {
                return new ResultSetCache.RowData(catalogName, schemaName, database, tableName, fkName);
            }

            @Override
            public boolean bulkContainsSchema(String schemaKey) {
                return database instanceof OracleDatabase;
            }

            @Override
            public String getSchemaKey(CachedRow row) {
                return row.getString("FKTABLE_SCHEM");
            }

            @Override
            public List<CachedRow> fastFetch() throws SQLException, DatabaseException {
                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                String jdbcCatalogName = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);

                if (database instanceof DB2Database) {
                    if (database.getDatabaseProductName().startsWith("DB2 UDB for AS/400")) {
                        executeAndExtract(getDB2ForAs400Sql(jdbcSchemaName, tableName), database);
                    }
                    return querytDB2Luw(jdbcSchemaName, tableName);
                } else if (database instanceof Db2zDatabase) {
                    return queryDb2Zos(catalogAndSchema, tableName);
                } else {
                    List<String> tables = new ArrayList<>();
                    if (tableName == null) {
                        for (CachedRow row : getTables(jdbcCatalogName, jdbcSchemaName, null)) {
                            tables.add(row.getString("TABLE_NAME"));
                        }
                    } else {
                        tables.add(tableName);
                    }

                    List<CachedRow> returnList = new ArrayList<>();
                    for (String foundTable : tables) {
                        if (database instanceof OracleDatabase) {
                            throw new RuntimeException("Should have bulk selected");
                        } else {
                            returnList.addAll(extract(databaseMetaData.getImportedKeys(jdbcCatalogName, jdbcSchemaName, foundTable)));
                        }
                    }

                    return returnList;
                }
            }

            @Override
            public List<CachedRow> bulkFetch() throws SQLException, DatabaseException {
                if (database instanceof OracleDatabase) {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    String sql = getOracleSql(jdbcSchemaName);
                    return executeAndExtract(sql, database);
                } else if (database instanceof DB2Database) {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    if (database.getDatabaseProductName().startsWith("DB2 UDB for AS/400")) {
                        executeAndExtract(getDB2ForAs400Sql(jdbcSchemaName, null), database);
                    }
                    return querytDB2Luw(jdbcSchemaName, null);
                } else if (database instanceof Db2zDatabase) {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    return queryDb2Zos(catalogAndSchema, null);
                } else if (database instanceof MSSQLDatabase) {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    String sql = getMSSQLSql(jdbcSchemaName, tableName);
                    return executeAndExtract(sql, database);
                } else {
                    throw new RuntimeException("Cannot bulk select");
                }
            }

            protected String getOracleSql(String jdbcSchemaName) {
                String sql = "SELECT  /*+rule*/" +
                        "  NULL AS pktable_cat,  " +
                        "  p.owner as pktable_schem,  " +
                        "  p.table_name as pktable_name,  " +
                        "  pc.column_name as pkcolumn_name,  " +
                        "  NULL as fktable_cat,  " +
                        "  f.owner as fktable_schem,  " +
                        "  f.table_name as fktable_name,  " +
                        "  fc.column_name as fkcolumn_name,  " +
                        "  fc.position as key_seq,  " +
                        "  NULL as update_rule,  " +
                        "  decode (f.delete_rule, 'CASCADE', 0, 'SET NULL', 2, 1) as delete_rule,  " +
                        "  f.constraint_name as fk_name,  " +
                        "  p.constraint_name as pk_name,  " +
                        "  decode(f.deferrable, 'DEFERRABLE', 5, 'NOT DEFERRABLE', 7, 'DEFERRED', 6) deferrability,  " +
                        "  f.validated as fk_validate " +
                        "FROM " +
                        "all_cons_columns pc " +
                        "INNER JOIN all_constraints p " +
                        "ON pc.owner = p.owner " +
                        "AND pc.constraint_name = p.constraint_name " +
                        "INNER JOIN all_constraints f " +
                        "ON pc.owner = f.r_owner " +
                        "AND pc.constraint_name = f.r_constraint_name " +
                        "INNER JOIN all_cons_columns fc " +
                        "ON fc.owner = f.owner " +
                        "AND fc.constraint_name = f.constraint_name " +
                        "AND fc.position = pc.position ";
                if (getAllCatalogsStringScratchData() == null) {
                    sql += "WHERE f.owner = '" + jdbcSchemaName + "' ";
                } else {
                    sql += "WHERE f.owner IN ('" + jdbcSchemaName + "', " + getAllCatalogsStringScratchData() + ") ";
                }
                sql += "AND p.constraint_type in ('P', 'U') " +
                        "AND f.constraint_type = 'R' " +
                        "AND p.table_name NOT LIKE 'BIN$%' " +
                        "ORDER BY fktable_schem, fktable_name, key_seq";
                return sql;
            }

            protected String getMSSQLSql(String jdbcSchemaName, String tableName) {
                //comes from select object_definition(object_id('sp_fkeys'))
                return "select " +
                        "convert(sysname,db_name()) AS PKTABLE_CAT, " +
                        "convert(sysname,schema_name(o1.schema_id)) AS PKTABLE_SCHEM, " +
                        "convert(sysname,o1.name) AS PKTABLE_NAME, " +
                        "convert(sysname,c1.name) AS PKCOLUMN_NAME, " +
                        "convert(sysname,db_name()) AS FKTABLE_CAT, " +
                        "convert(sysname,schema_name(o2.schema_id)) AS FKTABLE_SCHEM, " +
                        "convert(sysname,o2.name) AS FKTABLE_NAME, " +
                        "convert(sysname,c2.name) AS FKCOLUMN_NAME, " +
                        "isnull(convert(smallint,k.constraint_column_id), convert(smallint,0)) AS KEY_SEQ, " +
                        "convert(smallint, case ObjectProperty(f.object_id, 'CnstIsUpdateCascade') when 1 then 0 else 1 end) AS UPDATE_RULE, " +
                        "convert(smallint, case ObjectProperty(f.object_id, 'CnstIsDeleteCascade') when 1 then 0 else 1 end) AS DELETE_RULE, " +
                        "convert(sysname,object_name(f.object_id)) AS FK_NAME, " +
                        "convert(sysname,i.name) AS PK_NAME, " +
                        "convert(smallint, 7) AS DEFERRABILITY " +
                        "from " +
                        "sys.objects o1, " +
                        "sys.objects o2, " +
                        "sys.columns c1, " +
                        "sys.columns c2, " +
                        "sys.foreign_keys f inner join " +
                        "sys.foreign_key_columns k on (k.constraint_object_id = f.object_id) inner join " +
                        "sys.indexes i on (f.referenced_object_id = i.object_id and f.key_index_id = i.index_id) " +
                        "where " +
                        "o1.object_id = f.referenced_object_id and " +
                        "o2.object_id = f.parent_object_id and " +
                        "c1.object_id = f.referenced_object_id and " +
                        "c2.object_id = f.parent_object_id and " +
                        "c1.column_id = k.referenced_column_id and " +
                        "c2.column_id = k.parent_column_id and " +
                        "((object_schema_name(o1.object_id)='" + jdbcSchemaName + "'" +
                        " and convert(sysname,schema_name(o2.schema_id))='" + jdbcSchemaName + "' and " +
                        "convert(sysname,o2.name)='" + tableName + "' ) or ( convert(sysname,schema_name" +
                        "(o2.schema_id))='" + jdbcSchemaName + "' and convert(sysname,o2.name)='" + tableName +
                        "' )) order by 5, 6, 7, 9, 8";
            }

            private List<CachedRow> querytDB2Luw(String jdbcSchemaName, String tableName) throws DatabaseException, SQLException {
                List<String> parameters = new ArrayList<>(2);
                StringBuilder sql = new StringBuilder("SELECT " +
                        "  pk_col.tabschema AS pktable_cat,  " +
                        "  pk_col.tabname as pktable_name,  " +
                        "  pk_col.colname as pkcolumn_name, " +
                        "  fk_col.tabschema as fktable_cat,  " +
                        "  fk_col.tabname as fktable_name,  " +
                        "  fk_col.colname as fkcolumn_name, " +
                        "  fk_col.colseq as key_seq,  " +
                        "  decode (ref.updaterule, 'A', 3, 'R', 1, 1) as update_rule,  " +
                        "  decode (ref.deleterule, 'A', 3, 'C', 0, 'N', 2, 'R', 1, 1) as delete_rule,  " +
                        "  ref.constname as fk_name,  " +
                        "  ref.refkeyname as pk_name,  " +
                        "  7 as deferrability  " +
                        "FROM " +
                        "syscat.references ref " +
                        "join syscat.keycoluse fk_col on ref.constname=fk_col.constname and ref.tabschema=fk_col.tabschema and ref.tabname=fk_col.tabname " +
                        "join syscat.keycoluse pk_col on ref.refkeyname=pk_col.constname and ref.reftabschema=pk_col.tabschema and ref.reftabname=pk_col.tabname and pk_col.colseq=fk_col.colseq " +
                        "WHERE ref.tabschema = ? ");
                parameters.add(jdbcSchemaName);
                if (tableName != null) {
                    sql.append("and fk_col.tabname = ? ");
                    parameters.add(tableName);
                }
                sql.append("ORDER BY fk_col.colseq");
                return executeAndExtract(database, sql.toString(), parameters.toArray());
            }

            private String getDB2ForAs400Sql(String jdbcSchemaName, String tableName) {
                return "SELECT " +
                        "pktable_cat, " +
                        "pktable_name, " +
                        "pkcolumn_name, " +
                        "fktable_cat, " +
                        "fktable_name, " +
                        "fkcolumn_name, " +
                        "key_seq, " +
                        "update_rule, " +
                        "delete_rule, " +
                        "fk_name, " +
                        "pk_name, " +
                        "deferrability " +
                        "FROM " +
                        "sysibm.SQLFORKEYS " +
                        "WHERE " +
                        "FKTABLE_SCHEM = '" + jdbcSchemaName + "' " +
                        "AND FKTABLE_NAME = '" + tableName + "'";
            }

            protected List<CachedRow> queryDb2Zos(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {

                List<String> parameters = new ArrayList<>(2);
                StringBuilder sql = new StringBuilder("SELECT  " +
                        "  ref.REFTBCREATOR AS pktable_cat,  " +
                        "  ref.REFTBNAME as pktable_name,  " +
                        "  pk_col.colname as pkcolumn_name, " +
                        "  ref.CREATOR as fktable_cat,  " +
                        "  ref.TBNAME as fktable_name,  " +
                        "  fk_col.colname as fkcolumn_name, " +
                        "  fk_col.colseq as key_seq,  " +
                        "  decode (ref.deleterule, 'A', 3, 'C', 0, 'N', 2, 'R', 1, 1) as delete_rule,  " +
                        "  ref.relname as fk_name,  " +
                        "  pk_col.colname as pk_name,  " +
                        "  7 as deferrability  " +
                        "FROM " +
                        "SYSIBM.SYSRELS ref " +
                        "join SYSIBM.SYSFOREIGNKEYS fk_col " +
                        "on ref.relname = fk_col.RELNAME " +
                        "and ref.CREATOR = fk_col.CREATOR " +
                        "and ref.TBNAME = fk_col.TBNAME " +
                        "join SYSIBM.SYSKEYCOLUSE pk_col " +
                        "on ref.REFTBCREATOR = pk_col.TBCREATOR " +
                        "and ref.REFTBNAME = pk_col.TBNAME " +
                        "and pk_col.colseq=fk_col.colseq " +
                        "WHERE ref.CREATOR = ? ");
                parameters.add(((AbstractJdbcDatabase) CachingDatabaseMetaData.this.database).getJdbcSchemaName(catalogAndSchema));
                if (tableName != null) {
                    sql.append("AND ref.TBNAME = ? ");
                    parameters.add(tableName);
                }
                sql.append("ORDER BY fk_col.colseq");

                return executeAndExtract(CachingDatabaseMetaData.this.database, sql.toString(), parameters.toArray());
            }

            @Override
            protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                if (database instanceof AbstractDb2Database || database instanceof MSSQLDatabase) {
                    return super.shouldBulkSelect(schemaKey, resultSetCache); //can bulk and fast fetch
                } else {
                    return database instanceof OracleDatabase; //oracle is slow, always bulk select while you are at it. Other databases need to go through all tables.
                }
            }
        }

        private class GetNotNullConstraintsResultSetCache extends ResultSetCache.SingleResultSetExtractor {
            final String catalogName;
            final String schemaName;
            final String tableName;

            private GetNotNullConstraintsResultSetCache(Database database, String catalogName, String schemaName, String tableName) {
                super(database);
                this.catalogName = catalogName;
                this.schemaName = schemaName;
                this.tableName = tableName;
            }

            @Override
            public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEMA"),
                        database, row.getString("TABLE_NAME"));
            }

            @Override
            public ResultSetCache.RowData wantedKeyParameters() {
                return new ResultSetCache.RowData(catalogName, schemaName, database, tableName);
            }

            @Override
            public boolean bulkContainsSchema(String schemaKey) {
                return database instanceof OracleDatabase;
            }

            @Override
            public String getSchemaKey(CachedRow row) {
                return row.getString("TABLE_SCHEMA");
            }

            @Override
            protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                LiquibaseTableNamesFactory liquibaseTableNamesFactory = Scope.getCurrentScope().getSingleton(LiquibaseTableNamesFactory.class);
                List<String> liquibaseTableNames = liquibaseTableNamesFactory.getLiquibaseTableNames(database);
                return liquibaseTableNames.stream().noneMatch(tableName::equalsIgnoreCase);
            }

            @Override
            public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
                if (database instanceof OracleDatabase) {
                    return oracleQuery(false);
                }
                return Collections.emptyList();
            }

            @Override
            public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
                if (database instanceof OracleDatabase) {
                    return oracleQuery(true);
                }
                return Collections.emptyList();
            }

            private List<CachedRow> oracleQuery(boolean bulk) throws DatabaseException, SQLException {
                CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                String jdbcSchemaName = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                String jdbcTableName = database.escapeStringForDatabase(tableName);
                String sqlToSelectNotNullConstraints = "SELECT  NULL AS TABLE_CAT, atc.OWNER AS TABLE_SCHEMA, atc.OWNER, atc.TABLE_NAME, " +
                        "atc.COLUMN_NAME, NULLABLE, ac.VALIDATED as VALIDATED, ac.SEARCH_CONDITION, ac.CONSTRAINT_NAME " +
                        "FROM ALL_TAB_COLS atc " +
                        "JOIN all_cons_columns acc ON atc.OWNER = acc.OWNER AND atc.TABLE_NAME = acc.TABLE_NAME AND atc.COLUMN_NAME = acc.COLUMN_NAME " +
                        "JOIN all_constraints ac ON atc.OWNER = ac.OWNER AND atc.TABLE_NAME = ac.TABLE_NAME AND acc.CONSTRAINT_NAME = ac.CONSTRAINT_NAME ";

                if (!bulk || getAllCatalogsStringScratchData() == null) {
                    sqlToSelectNotNullConstraints += " WHERE atc.OWNER='" + jdbcSchemaName + "' AND atc.hidden_column='NO' AND ac.CONSTRAINT_TYPE='C'  and ac.search_condition is not null ";
                } else {
                    sqlToSelectNotNullConstraints += " WHERE atc.OWNER IN ('" + jdbcSchemaName + "', " + getAllCatalogsStringScratchData() + ") "
                            + " AND atc.hidden_column='NO' AND ac.CONSTRAINT_TYPE='C'  and ac.search_condition is not null ";
                }

                sqlToSelectNotNullConstraints += (!bulk && tableName != null && !tableName.isEmpty()) ? " AND atc.TABLE_NAME='" + jdbcTableName + "'" : "";

                return this.executeAndExtract(sqlToSelectNotNullConstraints, database);
            }

            @Override
            protected List<CachedRow> extract(ResultSet resultSet, boolean informixIndexTrimHint) throws SQLException {
                List<CachedRow> cachedRowList = new ArrayList<>();
                if (!(database instanceof OracleDatabase)) {
                    return cachedRowList;
                }

                resultSet.setFetchSize(database.getFetchSize());

                try {
                    List<Map> result = (List<Map>) new RowMapperNotNullConstraintsResultSetExtractor(new ColumnMapRowMapper(database.isCaseSensitive()) {
                        @Override
                        protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
                            Object value = super.getColumnValue(rs, index);
                            if (!(value instanceof String)) {
                                return value;
                            }
                            return value.toString().trim();
                        }
                    }).extractData(resultSet);

                    for (Map row : result) {
                        cachedRowList.add(new CachedRow(row));
                    }
                } finally {
                    JdbcUtil.closeResultSet(resultSet);
                }
                return cachedRowList;

            }
        }

        public List<CachedRow> getTables(final String catalogName, final String schemaName, final String table) throws DatabaseException {
            return getResultSetCache("getTables").get(new ResultSetCache.SingleResultSetExtractor(database) {

                @Override
                protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                    return table == null || getAllCatalogsStringScratchData() != null || super.shouldBulkSelect(schemaKey, resultSetCache);
                }

                @Override
                public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                    return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"), database, row.getString("TABLE_NAME"));
                }

                @Override
                public ResultSetCache.RowData wantedKeyParameters() {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, table);
                }

                @Override
                public boolean bulkContainsSchema(String schemaKey) {
                    return database instanceof OracleDatabase;
                }

                @Override
                public String getSchemaKey(CachedRow row) {
                    return row.getString("TABLE_SCHEM");
                }

                @Override
                public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    if (database instanceof OracleDatabase) {

                        // 是否dm
                        if (database.getConnection().getURL().startsWith("jdbc:dm")) {
                            return queryDM(catalogAndSchema, table);
                        } else {
                            return queryOracle(catalogAndSchema, table);
                        }
                    } else if (database instanceof MSSQLDatabase) {
                        return queryMssql(catalogAndSchema, table);
                    } else if (database instanceof Db2zDatabase) {
                        return queryDb2Zos(catalogAndSchema, table);
                    } else if (database instanceof PostgresDatabase) {
                        return queryPostgres(catalogAndSchema, table);
                    }

                    String catalog = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                    String schema = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    return extract(databaseMetaData.getTables(catalog, escapeForLike(schema, database), ((table == null) ?
                            SQL_FILTER_MATCH_ALL : escapeForLike(table, database)), new String[]{"TABLE"}));
                }

                @Override
                public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    if (database instanceof OracleDatabase) {
                        // 是否dm
                        if (database.getConnection().getURL().startsWith("jdbc:dm")) {
                            return queryDM(catalogAndSchema, null);
                        } else {
                            return queryOracle(catalogAndSchema, null);
                        }
                    } else if (database instanceof MSSQLDatabase) {
                        return queryMssql(catalogAndSchema, null);
                    } else if (database instanceof Db2zDatabase) {
                        return queryDb2Zos(catalogAndSchema, null);
                    } else if (database instanceof PostgresDatabase) {
                        return queryPostgres(catalogAndSchema, table);
                    }

                    String catalog = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                    String schema = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    return extract(databaseMetaData.getTables(catalog, escapeForLike(schema, database), SQL_FILTER_MATCH_ALL, new String[]{"TABLE"}));
                }

                private List<CachedRow> queryMssql(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getSchemaName(), Schema.class);

                    String databaseName = StringUtil.trimToNull(database.correctObjectName(catalogAndSchema.getCatalogName(), Catalog.class));
                    String dbIdParam;
                    String databasePrefix;
                    if (databaseName == null) {
                        databasePrefix = "";
                        dbIdParam = "";
                    } else {
                        dbIdParam = ", db_id('" + databaseName + "')";
                        databasePrefix = "[" + databaseName + "].";
                    }


                    //From select object_definition(object_id('sp_tables'))
                    String sql = "select " +
                            "db_name(" + (databaseName == null ? "" : "db_id('" + databaseName + "')") + ") AS TABLE_CAT, " +
                            "convert(sysname,object_schema_name(o.object_id" + dbIdParam + ")) AS TABLE_SCHEM, " +
                            "convert(sysname,o.name) AS TABLE_NAME, " +
                            "'TABLE' AS TABLE_TYPE, " +
                            "CAST(ep.value as varchar(max)) as REMARKS " +
                            "from " + databasePrefix + "sys.all_objects o " +
                            "left outer join sys.extended_properties ep on ep.name='MS_Description' and major_id=o.object_id and minor_id=0 " +
                            "where " +
                            "o.type in ('U') " +
                            "and has_perms_by_name(" + (databaseName == null ? "" : "quotename('" + databaseName + "') + '.' + ") + "quotename(object_schema_name(o.object_id" + dbIdParam + ")) + '.' + quotename(o.name), 'object', 'select') = 1 " +
                            "and charindex(substring(o.type,1,1),'U') <> 0 " +
                            "and object_schema_name(o.object_id" + dbIdParam + ")='" + database.escapeStringForDatabase(ownerName) + "'";
                    if (tableName != null) {
                        sql += " AND o.name='" + database.escapeStringForDatabase(tableName) + "' ";
                    }
                    sql += "order by 4, 1, 2, 3";

                    return executeAndExtract(sql, database);
                }


                private List<CachedRow> queryDM(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class);

                    String sql = "SELECT null as TABLE_CAT, a.OWNER as TABLE_SCHEM, a.TABLE_NAME as TABLE_NAME, " +
                            "a.TEMPORARY as TEMPORARY, a.DURATION as DURATION, 'TABLE' as TABLE_TYPE, " +
                            "c.COMMENTS as REMARKS, A.tablespace_name as tablespace_name, CASE WHEN A.tablespace_name = " +
                            "(SELECT DEFAULT_TABLESPACE FROM USER_USERS) THEN 'true' ELSE null END as default_tablespace " +
                            "from ALL_TABLES a " +
                            "join ALL_TAB_COMMENTS c on a.TABLE_NAME=c.table_name and a.owner=c.owner ";
                    String allCatalogsString = getAllCatalogsStringScratchData();
                    if (tableName != null || allCatalogsString == null) {
                        sql += "AND a.OWNER='" + ownerName + "'";
                    } else {
                        sql += "AND a.OWNER IN ('" + ownerName + "', " + allCatalogsString + ")";
                    }
                    if (tableName != null) {
                        sql += " AND a.TABLE_NAME='" + tableName + "'";
                    }

                    return executeAndExtract(sql, database);
                }

                private List<CachedRow> queryOracle(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class);

                    String sql = "SELECT null as TABLE_CAT, a.OWNER as TABLE_SCHEM, a.TABLE_NAME as TABLE_NAME, " +
                            "a.TEMPORARY as TEMPORARY, a.DURATION as DURATION, 'TABLE' as TABLE_TYPE, " +
                            "c.COMMENTS as REMARKS, A.tablespace_name as tablespace_name, CASE WHEN A.tablespace_name = " +
                            "(SELECT DEFAULT_TABLESPACE FROM USER_USERS) THEN 'true' ELSE null END as default_tablespace " +
                            "from ALL_TABLES a " +
                            "join ALL_TAB_COMMENTS c on a.TABLE_NAME=c.table_name and a.owner=c.owner " +
                            "left outer join ALL_QUEUE_TABLES q ON a.TABLE_NAME = q.QUEUE_TABLE and a.OWNER = q.OWNER " +
                            "WHERE q.QUEUE_TABLE is null ";
                    String allCatalogsString = getAllCatalogsStringScratchData();
                    if (tableName != null || allCatalogsString == null) {
                        sql += "AND a.OWNER='" + ownerName + "'";
                    } else {
                        sql += "AND a.OWNER IN ('" + ownerName + "', " + allCatalogsString + ")";
                    }
                    if (tableName != null) {
                        sql += " AND a.TABLE_NAME='" + tableName + "'";
                    }

                    return executeAndExtract(sql, database);
                }

                private List<CachedRow> queryDb2Zos(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class);

                    String sql = "SELECT CREATOR AS TABLE_SCHEM, " +
                            "NAME AS TABLE_NAME, " +
                            "'TABLE' AS TABLE_TYPE, " +
                            "REMARKS " +
                            "FROM SYSIBM.SYSTABLES " +
                            "WHERE TYPE = 'T'";
                    List<String> parameters = new ArrayList<>(2);
                    if (ownerName != null) {
                        sql += " AND CREATOR = ?";
                        parameters.add(ownerName);
                    }
                    if (tableName != null) {
                        sql += " AND NAME = ?";
                        parameters.add(tableName);
                    }

                    return executeAndExtract(database, sql, parameters.toArray());
                }

                private List<CachedRow> queryPostgres(CatalogAndSchema catalogAndSchema, String tableName) throws SQLException {
                    String catalog = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                    String schema = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    return extract(databaseMetaData.getTables(catalog, escapeForLike(schema, database), ((tableName == null) ?
                            SQL_FILTER_MATCH_ALL : escapeForLike(tableName, database)), new String[]{"TABLE", "PARTITIONED TABLE"}));

                }
            });
        }

        public List<CachedRow> getViews(final String catalogName, final String schemaName, String viewName) throws DatabaseException {
            final String view;
            if (database instanceof DB2Database) {
                view = database.correctObjectName(viewName, View.class);
            } else {
                view = viewName;
            }
            return getResultSetCache("getViews").get(new ResultSetCache.SingleResultSetExtractor(database) {

                @Override
                protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                    return view == null || getAllCatalogsStringScratchData() != null || super.shouldBulkSelect(schemaKey, resultSetCache);
                }

                @Override
                public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                    return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"), database, row.getString("TABLE_NAME"));
                }


                @Override
                public ResultSetCache.RowData wantedKeyParameters() {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, view);
                }

                @Override
                public boolean bulkContainsSchema(String schemaKey) {
                    return database instanceof OracleDatabase;
                }

                @Override
                public String getSchemaKey(CachedRow row) {
                    return row.getString("TABLE_SCHEM");
                }


                @Override
                public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    if (database instanceof OracleDatabase) {
                        return queryOracle(catalogAndSchema, view);
                    } else if (database instanceof MSSQLDatabase) {
                        return queryMssql(catalogAndSchema, view);
                    }

                    String catalog = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                    String schema = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    return extract(databaseMetaData.getTables(catalog, escapeForLike(schema, database), ((view == null) ? SQL_FILTER_MATCH_ALL
                            : escapeForLike(view, database)), new String[]{"VIEW"}));
                }

                @Override
                public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    if (database instanceof OracleDatabase) {
                        return queryOracle(catalogAndSchema, null);
                    } else if (database instanceof MSSQLDatabase) {
                        return queryMssql(catalogAndSchema, null);
                    }

                    String catalog = ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema);
                    String schema = ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema);
                    return extract(databaseMetaData.getTables(catalog, escapeForLike(schema, database), SQL_FILTER_MATCH_ALL, new String[]{"VIEW"}));
                }

                private List<CachedRow> queryMssql(CatalogAndSchema catalogAndSchema, String viewName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getSchemaName(), Schema.class);
                    String databaseName = StringUtil.trimToNull(database.correctObjectName(catalogAndSchema.getCatalogName(), Catalog.class));
                    String dbIdParam = "";
                    String databasePrefix = "";
                    boolean haveDatabaseName = databaseName != null;

                    if (haveDatabaseName) {
                        dbIdParam = ", db_id('" + databaseName + "')";
                        databasePrefix = "[" + databaseName + "].";
                    }
                    String tableCatParam = haveDatabaseName ? "db_id('" + databaseName + "')" : "";
                    String permsParam = haveDatabaseName ? "quotename('" + databaseName + "') + '.' + " : "";

                    String sql = "select " +
                            "db_name(" + tableCatParam + ") AS TABLE_CAT, " +
                            "convert(sysname,object_schema_name(o.object_id" + dbIdParam + ")) AS TABLE_SCHEM, " +
                            "convert(sysname,o.name) AS TABLE_NAME, " +
                            "'VIEW' AS TABLE_TYPE, " +
                            "CAST(ep.value as varchar(max)) as REMARKS " +
                            "from " + databasePrefix + "sys.all_objects o " +
                            "left join sys.extended_properties ep on ep.name='MS_Description' and major_id=o.object_id and minor_id=0 " +
                            "where " +
                            "o.type in ('V') " +
                            "and has_perms_by_name(" + permsParam + "quotename(object_schema_name(o.object_id" + dbIdParam + ")) + '.' + quotename(o.name), 'object', 'select') = 1 " +
                            "and charindex(substring(o.type,1,1),'V') <> 0 " +
                            "and object_schema_name(o.object_id" + dbIdParam + ")='" + database.escapeStringForDatabase(ownerName) + "'";
                    if (viewName != null) {
                        sql += " AND o.name='" + database.escapeStringForDatabase(viewName) + "' ";
                    }
                    sql += "order by 4, 1, 2, 3";

                    return executeAndExtract(sql, database);
                }


                private List<CachedRow> queryOracle(CatalogAndSchema catalogAndSchema, String viewName) throws DatabaseException, SQLException {
                    String ownerName = database.correctObjectName(catalogAndSchema.getCatalogName(), Schema.class);

                    String sql = "SELECT null as TABLE_CAT, a.OWNER as TABLE_SCHEM, a.VIEW_NAME as TABLE_NAME, 'TABLE' as TABLE_TYPE, c.COMMENTS as REMARKS, TEXT as OBJECT_BODY";
                    if (database.getDatabaseMajorVersion() > 10) {
                        sql += ", EDITIONING_VIEW";
                    }
                    sql += " from ALL_VIEWS a " +
                            "join ALL_TAB_COMMENTS c on a.VIEW_NAME=c.table_name and a.owner=c.owner ";
                    if (viewName != null || getAllCatalogsStringScratchData() == null) {
                        sql += "WHERE a.OWNER='" + ownerName + "'";
                    } else {
                        sql += "WHERE a.OWNER IN ('" + ownerName + "', " + getAllCatalogsStringScratchData() + ")";
                    }
                    if (viewName != null) {
                        sql += " AND a.VIEW_NAME='" + database.correctObjectName(viewName, View.class) + "'";
                    }
                    sql += " AND a.VIEW_NAME not in (select mv.name from all_registered_mviews mv where mv.owner=a.owner)";

                    return executeAndExtract(sql, database);
                }
            });
        }

        public List<CachedRow> getPrimaryKeys(final String catalogName, final String schemaName, final String table) throws DatabaseException {
            return getResultSetCache("getPrimaryKeys").get(new ResultSetCache.SingleResultSetExtractor(database) {

                @Override
                public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                    return new ResultSetCache.RowData(row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"), database, row.getString("TABLE_NAME"));
                }

                @Override
                public ResultSetCache.RowData wantedKeyParameters() {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, table);
                }

                @Override
                public boolean bulkContainsSchema(String schemaKey) {
                    return database instanceof OracleDatabase;
                }


                @Override
                public String getSchemaKey(CachedRow row) {
                    return row.getString("TABLE_SCHEM");
                }

                @Override
                public List<CachedRow> fastFetchQuery() throws SQLException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                    try {
                        List<CachedRow> foundPks = new ArrayList<>();
                        if (table == null) {
                            List<CachedRow> tables = CachingDatabaseMetaData.this.getTables(catalogName, schemaName, null);
                            for (CachedRow table : tables) {
                                List<CachedRow> pkInfo = getPkInfo(catalogAndSchema, table.getString("TABLE_NAME"));
                                if (pkInfo != null) {
                                    foundPks.addAll(pkInfo);
                                }
                            }
                            return foundPks;
                        } else {
                            List<CachedRow> pkInfo = getPkInfo(catalogAndSchema, table);
                            if (pkInfo != null) {
                                foundPks.addAll(pkInfo);
                            }
                        }
                        return foundPks;
                    } catch (DatabaseException e) {
                        throw new SQLException(e);
                    }
                }

                private List<CachedRow> getPkInfo(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException, SQLException {
                    List<CachedRow> pkInfo;
                    if (database instanceof MSSQLDatabase) {
                        String sql = mssqlSql(catalogAndSchema, tableName);
                        pkInfo = executeAndExtract(sql, database);
                    } else {
                        if (database instanceof Db2zDatabase) {
                            String sql = "SELECT 'NULL' AS TABLE_CAT," +
                                    " SYSTAB.TBCREATOR AS TABLE_SCHEM, " +
                                    "SYSTAB.TBNAME AS TABLE_NAME, " +
                                    "COLUSE.COLNAME AS COLUMN_NAME, " +
                                    "COLUSE.COLSEQ AS KEY_SEQ, " +
                                    "SYSTAB.CONSTNAME AS PK_NAME " +
                                    "FROM SYSIBM.SYSTABCONST SYSTAB " +
                                    "JOIN SYSIBM.SYSKEYCOLUSE COLUSE " +
                                    "ON SYSTAB.TBCREATOR = COLUSE.TBCREATOR " +
                                    "WHERE SYSTAB.TYPE = 'P' " +
                                    "AND SYSTAB.TBNAME = ? " +
                                    "AND SYSTAB.TBCREATOR = ? " +
                                    "AND SYSTAB.TBNAME=COLUSE.TBNAME " +
                                    "AND SYSTAB.CONSTNAME=COLUSE.CONSTNAME " +
                                    "ORDER BY COLUSE.COLNAME";
                            try {
                                return executeAndExtract(database, sql, table, ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema));
                            } catch (DatabaseException e) {
                                throw new SQLException(e);
                            }
                        } else if (database instanceof OracleDatabase) {
                            warnAboutDbaRecycleBin();

                            String sql = "SELECT NULL AS table_cat, c.owner AS table_schem, c.table_name, c.column_name as COLUMN_NAME, c.position AS key_seq, c.constraint_name AS pk_name, k.VALIDATED as VALIDATED " +
                                    "FROM all_cons_columns c, all_constraints k " +
                                    "LEFT JOIN " + (((OracleDatabase) database).canAccessDbaRecycleBin() ? "dba_recyclebin" : "user_recyclebin") + " d ON d.object_name=k.table_name " +
                                    "WHERE k.constraint_type = 'P' " +
                                    "AND d.object_name IS NULL " +
                                    "AND k.table_name = '" + table + "' " +
                                    "AND k.owner = '" + ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema) + "' " +
                                    "AND k.constraint_name = c.constraint_name " +
                                    "AND k.table_name = c.table_name " +
                                    "AND k.owner = c.owner " +
                                    "ORDER BY column_name";
                            try {
                                return executeAndExtract(sql, database);
                            } catch (DatabaseException e) {
                                throw new SQLException(e);
                            }
                        } else if (database instanceof CockroachDatabase) {
                            // This is the same as the query generated by PGJDBC's getPrimaryKeys method, except it
                            // also adds an `asc_or_desc` column to the result.
                            String sql = "SELECT " +
                                    "  result.table_cat, " +
                                    "  result.table_schem, " +
                                    "  result.table_name, " +
                                    "  result.column_name, " +
                                    "  result.key_seq, " +
                                    "  result.pk_name, " +
                                    "  CASE result.indoption[result.key_seq - 1] & 1 " +
                                    "    WHEN 1 THEN 'D' " +
                                    "    ELSE 'A' " +
                                    "    END AS asc_or_desc " +
                                    "FROM " +
                                    "  (" +
                                    "    SELECT " +
                                    "      NULL AS table_cat, " +
                                    "      n.nspname AS table_schem, " +
                                    "      ct.relname AS table_name, " +
                                    "      a.attname AS column_name, " +
                                    "      (information_schema._pg_expandarray(i.indkey)).n " +
                                    "        AS key_seq, " +
                                    "      ci.relname AS pk_name, " +
                                    "      information_schema._pg_expandarray(i.indkey) AS keys, " +
                                    "      i.indoption, " +
                                    "      a.attnum AS a_attnum " +
                                    "    FROM " +
                                    "      pg_catalog.pg_class AS ct " +
                                    "      JOIN pg_catalog.pg_attribute AS a ON (ct.oid = a.attrelid) " +
                                    "      JOIN pg_catalog.pg_namespace AS n ON " +
                                    "          (ct.relnamespace = n.oid) " +
                                    "      JOIN pg_catalog.pg_index AS i ON (a.attrelid = i.indrelid) " +
                                    "      JOIN pg_catalog.pg_class AS ci ON (ci.oid = i.indexrelid) " +
                                    "    WHERE " +
                                    "      true " +
                                    "      AND n.nspname = '" + ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema) + "' " +
                                    "      AND ct.relname = '" + table + "' " +
                                    "      AND i.indisprimary" +
                                    "  ) " +
                                    "    AS result " +
                                    "WHERE " +
                                    "  result.a_attnum = (result.keys).x " +
                                    "ORDER BY " +
                                    "  result.table_name, result.pk_name, result.key_seq";

                            try {
                                return executeAndExtract(sql, database);
                            } catch (DatabaseException e) {
                                throw new SQLException(e);
                            }
                        } else {
                            return extract(
                                    databaseMetaData.getPrimaryKeys(
                                            ((AbstractJdbcDatabase) database).getJdbcCatalogName(catalogAndSchema),
                                            ((AbstractJdbcDatabase) database).getJdbcSchemaName(catalogAndSchema),
                                            table
                                    )
                            );
                        }
                    }
                    return pkInfo;
                }

                private String mssqlSql(CatalogAndSchema catalogAndSchema, String tableName) throws DatabaseException {
                    String sql;
                    sql =
                            "SELECT " +
                                    "DB_NAME() AS [TABLE_CAT], " +
                                    "[s].[name] AS [TABLE_SCHEM], " +
                                    "[t].[name] AS [TABLE_NAME], " +
                                    "[c].[name] AS [COLUMN_NAME], " +
                                    "CASE [ic].[is_descending_key] WHEN 0 THEN N'A' WHEN 1 THEN N'D' END AS [ASC_OR_DESC], " +
                                    "[ic].[key_ordinal] AS [KEY_SEQ], " +
                                    "[kc].[name] AS [PK_NAME] " +
                                    "FROM [sys].[schemas] AS [s] " +
                                    "INNER JOIN [sys].[tables] AS [t] " +
                                    "ON [t].[schema_id] = [s].[schema_id] " +
                                    "INNER JOIN [sys].[key_constraints] AS [kc] " +
                                    "ON [kc].[parent_object_id] = [t].[object_id] " +
                                    "INNER JOIN [sys].[indexes] AS [i] " +
                                    "ON [i].[object_id] = [kc].[parent_object_id] " +
                                    "AND [i].[index_id] = [kc].[unique_index_id] " +
                                    "INNER JOIN [sys].[index_columns] AS [ic] " +
                                    "ON [ic].[object_id] = [i].[object_id] " +
                                    "AND [ic].[index_id] = [i].[index_id] " +
                                    "INNER JOIN [sys].[columns] AS [c] " +
                                    "ON [c].[object_id] = [ic].[object_id] " +
                                    "AND [c].[column_id] = [ic].[column_id] " +
                                    "WHERE [s].[name] = N'" + database.escapeStringForDatabase(catalogAndSchema.getSchemaName()) + "' " + // The schema name was corrected in the customized CatalogAndSchema
                                    (tableName == null ? "" : "AND [t].[name] = N'" + database.escapeStringForDatabase(database.correctObjectName(tableName, Table.class)) + "' ") +
                                    "AND [kc].[type] = 'PK' " +
                                    "AND [ic].[key_ordinal] > 0 " +
                                    "ORDER BY " +
                                    "[ic].[key_ordinal]";
                    return sql;
                }

                @Override
                public List<CachedRow> bulkFetchQuery() throws SQLException {
                    if (database instanceof OracleDatabase) {
                        CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                        warnAboutDbaRecycleBin();
                        try {
                            String sql = "SELECT NULL AS table_cat, c.owner AS table_schem, c.table_name, c.column_name, c.position AS key_seq,c.constraint_name AS pk_name, k.VALIDATED as VALIDATED FROM " +
                                    "all_cons_columns c, " +
                                    "all_constraints k " +
                                    "LEFT JOIN " + (((OracleDatabase) database).canAccessDbaRecycleBin() ? "dba_recyclebin" : "user_recyclebin") + " d ON d.object_name=k.table_name " +
                                    "WHERE k.constraint_type = 'P' " +
                                    "AND d.object_name IS NULL ";
                            if (getAllCatalogsStringScratchData() == null) {
                                sql += "AND k.owner='" + catalogAndSchema.getCatalogName() + "' ";
                            } else {
                                sql += "AND k.owner IN ('" + catalogAndSchema.getCatalogName() + "', " + getAllCatalogsStringScratchData() + ")";
                            }
                            sql += "AND k.constraint_name = c.constraint_name " +
                                    "AND k.table_name = c.table_name " +
                                    "AND k.owner = c.owner " +
                                    "ORDER BY column_name";
                            return executeAndExtract(sql, database);
                        } catch (DatabaseException e) {
                            throw new SQLException(e);
                        }
                    } else if (database instanceof MSSQLDatabase) {
                        CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);
                        try {
                            return executeAndExtract(mssqlSql(catalogAndSchema, null), database);
                        } catch (DatabaseException e) {
                            throw new SQLException(e);
                        }
                    }
                    return null;
                }

                @Override
                protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                    if ((database instanceof OracleDatabase) || (database instanceof MSSQLDatabase)) {
                        return table == null || getAllCatalogsStringScratchData() != null || super.shouldBulkSelect(schemaKey, resultSetCache);
                    } else {
                        return false;
                    }
                }
            });
        }

        public List<CachedRow> getUniqueConstraints(final String catalogName, final String schemaName, final String tableName) throws DatabaseException {
            return getResultSetCache("getUniqueConstraints").get(new ResultSetCache.SingleResultSetExtractor(database) {

                @Override
                protected boolean shouldBulkSelect(String schemaKey, ResultSetCache resultSetCache) {
                    return tableName == null || getAllCatalogsStringScratchData() != null || super.shouldBulkSelect(schemaKey, resultSetCache);
                }

                @Override
                public boolean bulkContainsSchema(String schemaKey) {
                    return database instanceof OracleDatabase;
                }

                @Override
                public String getSchemaKey(CachedRow row) {
                    return row.getString("CONSTRAINT_SCHEM");
                }

                @Override
                public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, row.getString("TABLE_NAME"));
                }

                @Override
                public ResultSetCache.RowData wantedKeyParameters() {
                    return new ResultSetCache.RowData(catalogName, schemaName, database, tableName);
                }

                @Override
                public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    return queryDb(catalogAndSchema, tableName);
                }

                @Override
                public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
                    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName, schemaName).customize(database);

                    return queryDb(catalogAndSchema, null);
                }

                private List<CachedRow> queryDb(CatalogAndSchema catalogAndSchema, String tableName) throws SQLException, DatabaseException {

                    String jdbcCatalogName = catalogAndSchema.getCatalogName();
                    String jdbcSchemaName = catalogAndSchema.getSchemaName();

                    Database database = getDatabase();
                    List<String> parameters = new ArrayList<>(3);
                    String sql = null;
                    if (database instanceof Ingres9Database) {
                        sql = "select CONSTRAINT_NAME, TABLE_NAME from iiconstraints where schema_name ='"
                                + schemaName + "' and constraint_type='U'";
                        if (tableName != null) {
                            sql += " and table_name='" + tableName + "'";
                        }
                    } else if ((database instanceof MySQLDatabase) || (database instanceof HsqlDatabase) || (database
                            instanceof MariaDBDatabase)) {
                        sql = "select CONSTRAINT_NAME, TABLE_NAME "
                                + "from " + database.getSystemSchema() + ".table_constraints "
                                + "where constraint_schema='" + jdbcCatalogName + "' "
                                + "and constraint_type='UNIQUE'";
                        if (tableName != null) {
                            sql += " and table_name='" + tableName + "'";
                        }
                    } else if (database instanceof PostgresDatabase) {
                        sql = "select CONSTRAINT_NAME, TABLE_NAME "
                                + "from " + database.getSystemSchema() + ".table_constraints "
                                + "where constraint_catalog='" + jdbcCatalogName + "' "
                                + "and constraint_schema='" + jdbcSchemaName + "' "
                                + "and constraint_type='UNIQUE'";
                        if (tableName != null) {
                            sql += " and table_name='" + tableName + "'";
                        }
                    } else if (database.getClass().getName().contains("MaxDB")) { //have to check classname as this is currently an extension
                        sql = "select distinct tablename AS TABLE_NAME, constraintname AS CONSTRAINT_NAME from CONSTRAINTCOLUMNS WHERE CONSTRAINTTYPE = 'UNIQUE_CONST'";
                        if (tableName != null) {
                            sql += " and tablename='" + tableName + "'";
                        }
                    } else if (database instanceof MSSQLDatabase) {
                        sql =
                                "SELECT " +
                                        "[TC].[CONSTRAINT_NAME], " +
                                        "[TC].[TABLE_NAME], " +
                                        "[TC].[CONSTRAINT_CATALOG] AS INDEX_CATALOG, " +
                                        "[TC].[CONSTRAINT_SCHEMA] AS INDEX_SCHEMA, " +
                                        "[IDX].[TYPE_DESC], " +
                                        "[IDX].[name] AS INDEX_NAME " +
                                        "FROM [INFORMATION_SCHEMA].[TABLE_CONSTRAINTS] AS [TC] " +
                                        "JOIN sys.indexes AS IDX ON IDX.name=[TC].[CONSTRAINT_NAME] AND object_schema_name(object_id)=[TC].[CONSTRAINT_SCHEMA] " +
                                        "WHERE [TC].[CONSTRAINT_TYPE] = 'UNIQUE' " +
                                        "AND [TC].[CONSTRAINT_CATALOG] = N'" + database.escapeStringForDatabase(jdbcCatalogName) + "' " +
                                        "AND [TC].[CONSTRAINT_SCHEMA] = N'" + database.escapeStringForDatabase(jdbcSchemaName) + "'";
                        if (tableName != null) {
                            sql += " AND [TC].[TABLE_NAME] = N'" + database.escapeStringForDatabase(database.correctObjectName(tableName, Table.class)) + "'";
                        }
                    } else if (database instanceof OracleDatabase) {
                        warnAboutDbaRecycleBin();

                        sql = "select uc.owner AS CONSTRAINT_SCHEM, uc.constraint_name, uc.table_name,uc.status,uc.deferrable,uc.deferred,ui.tablespace_name, ui.index_name, ui.owner as INDEX_CATALOG, uc.VALIDATED as VALIDATED, ac.COLUMN_NAME as COLUMN_NAME " +
                                "from all_constraints uc " +
                                "join all_indexes ui on uc.index_name = ui.index_name and uc.owner=ui.table_owner and uc.table_name=ui.table_name " +
                                "LEFT OUTER JOIN " + (((OracleDatabase) database).canAccessDbaRecycleBin() ? "dba_recyclebin" : "user_recyclebin") + " d ON d.object_name=ui.table_name " +
                                "LEFT JOIN all_cons_columns ac ON ac.OWNER = uc.OWNER AND ac.TABLE_NAME = uc.TABLE_NAME AND ac.CONSTRAINT_NAME = uc.CONSTRAINT_NAME " +
                                "where uc.constraint_type='U' ";
                        if (tableName != null || getAllCatalogsStringScratchData() == null) {
                            sql += "and uc.owner = '" + jdbcSchemaName + "'";
                        } else {
                            sql += "and uc.owner IN ('" + jdbcSchemaName + "', " + getAllCatalogsStringScratchData() + ")";
                        }
                        sql += "AND d.object_name IS NULL ";

                        if (tableName != null) {
                            sql += " and uc.table_name = '" + tableName + "'";
                        }
                    } else if (database instanceof DB2Database) {
                        // if we are on DB2 AS400 iSeries
                        if (database.getDatabaseProductName().startsWith("DB2 UDB for AS/400")) {
                            sql = "select constraint_name as constraint_name, table_name as table_name from QSYS2.TABLE_CONSTRAINTS where table_schema='" + jdbcSchemaName + "' and constraint_type='UNIQUE'";
                            if (tableName != null) {
                                sql += " and table_name = '" + tableName + "'";
                            }
                            // DB2 z/OS
                        }
                        // here we are on DB2 UDB
                        else {
                            sql = "select distinct k.constname as constraint_name, t.tabname as TABLE_NAME "
                                    + "from syscat.keycoluse k "
                                    + "inner join syscat.tabconst t "
                                    + "on k.constname = t.constname "
                                    + "where t.tabschema = ? "
                                    + "and t.type = 'U'";
                            parameters.add(jdbcSchemaName);
                            if (tableName != null) {
                                sql += " and t.tabname = ?";
                                parameters.add(tableName);
                            }
                        }
                    } else if (database instanceof Db2zDatabase) {
                        sql = "select k.constname as constraint_name, t.tbname as TABLE_NAME"
                                + " from SYSIBM.SYSKEYCOLUSE k"
                                + " inner join SYSIBM.SYSTABCONST t"
                                + " on k.constname = t.constname"
                                + " and k.TBCREATOR = t.TBCREATOR"
                                + " and k.TBNAME = t.TBNAME"
                                + " where t.TBCREATOR = ?"
                                + " and t.TYPE = 'U'";
                        parameters.add(jdbcSchemaName);
                        if (tableName != null) {
                            sql += " and t.TBNAME = ?";
                            parameters.add(tableName);
                        }
                    } else if (database instanceof FirebirdDatabase) {
                        sql = "SELECT TRIM(RDB$INDICES.RDB$INDEX_NAME) AS CONSTRAINT_NAME, " +
                                "TRIM(RDB$INDICES.RDB$RELATION_NAME) AS TABLE_NAME " +
                                "FROM RDB$INDICES "
                                + "LEFT JOIN RDB$RELATION_CONSTRAINTS "
                                + "ON RDB$RELATION_CONSTRAINTS.RDB$INDEX_NAME = RDB$INDICES.RDB$INDEX_NAME "
                                + "WHERE RDB$INDICES.RDB$UNIQUE_FLAG IS NOT NULL "
                                + "AND ("
                                + "RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE IS NULL "
                                + "OR TRIM(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE)='UNIQUE') "
                                + "AND NOT(RDB$INDICES.RDB$INDEX_NAME LIKE 'RDB$%')";
                        if (tableName != null) {
                            sql += " AND TRIM(RDB$INDICES.RDB$RELATION_NAME)='" + tableName + "'";
                        }
                    } else if (database instanceof DerbyDatabase) {
                        sql = "select c.constraintname as CONSTRAINT_NAME, tablename AS TABLE_NAME "
                                + "from sys.systables t, sys.sysconstraints c, sys.sysschemas s "
                                + "where s.schemaname='" + jdbcCatalogName + "' "
                                + "and t.tableid = c.tableid "
                                + "and t.schemaid=s.schemaid "
                                + "and c.type = 'U'";
                        if (tableName != null) {
                            sql += " AND t.tablename = '" + tableName + "'";
                        }
                    } else if (database instanceof InformixDatabase) {
                        sql = "select unique sysindexes.idxname as CONSTRAINT_NAME, sysindexes.idxtype, systables.tabname as TABLE_NAME "
                                + "from sysindexes, systables "
                                + "left outer join sysconstraints on sysconstraints.tabid = systables.tabid and sysconstraints.constrtype = 'P' "
                                + "where sysindexes.tabid = systables.tabid and sysindexes.idxtype = 'U' "
                                + "and sysconstraints.idxname != sysindexes.idxname "
                                + "and sysconstraints.tabid = sysindexes.tabid";
                        if (tableName != null) {
                            sql += " and systables.tabname = '" + database.correctObjectName(tableName, Table.class) + "'";
                        }
                    } else if (database instanceof SybaseDatabase) {
                        sql = "select idx.name as CONSTRAINT_NAME, tbl.name as TABLE_NAME "
                                + "from sysindexes idx "
                                + "inner join sysobjects tbl on tbl.id = idx.id "
                                + "where idx.indid between 1 and 254 "
                                + "and (idx.status & 2) = 2 "
                                + "and tbl.type = 'U'";
                        if (tableName != null) {
                            sql += " and tbl.name = '" + database.correctObjectName(tableName, Table.class) + "'";
                        }
                    } else if (database instanceof SybaseASADatabase) {
                        sql = "select sysconstraint.constraint_name, sysconstraint.constraint_type, systable.table_name " +
                                "from sysconstraint, systable " +
                                "where sysconstraint.table_object_id = systable.object_id " +
                                "and sysconstraint.constraint_type = 'U'";
                        if (tableName != null) {
                            sql += " and systable.table_name = '" + tableName + "'";
                        }
                    } else {
                        if (database instanceof H2Database) {
                            try {
                                if (database.getDatabaseMajorVersion() >= 2) {
                                    sql = "select CONSTRAINT_NAME, CONSTRAINT_TYPE, TABLE_NAME "
                                            + "from " + database.getSystemSchema() + ".table_constraints "
                                            + "where constraint_schema='" + jdbcSchemaName + "' "
                                            + "and constraint_catalog='" + jdbcCatalogName + "' "
                                            + "and constraint_type='UNIQUE'";
                                    if (tableName != null) {
                                        sql += " and table_name='" + tableName + "'";
                                    }
                                }
                            } catch (DatabaseException e) {
                                Scope.getCurrentScope().getLog(getClass()).fine("Cannot determine h2 version, using default unique constraint query");
                            }
                        }
                        if (sql == null) {

                            sql = "select CONSTRAINT_NAME, CONSTRAINT_TYPE, TABLE_NAME "
                                    + "from " + database.getSystemSchema() + ".constraints "
                                    + "where constraint_schema='" + jdbcSchemaName + "' "
                                    + "and constraint_catalog='" + jdbcCatalogName + "' "
                                    + "and constraint_type='UNIQUE'";
                            if (tableName != null) {
                                sql += " and table_name='" + tableName + "'";
                            }
                        }
                    }

                    return executeAndExtract(database, database instanceof InformixDatabase, sql, parameters.toArray());
                }
            });
        }
    }

    private String getAllCatalogsStringScratchData() {
        return (String) getScratchData(ALL_CATALOGS_STRING_SCRATCH_KEY);
    }

    private String escapeForLike(String string, Database database) {
        if (string == null) {
            return null;
        }
        // 是否dm
        if (database instanceof SQLiteDatabase
                || database.getConnection().getURL().startsWith("jdbc:dm")) {
            //sqlite jdbc's queries does not support escaped patterns.
            return string;
        }

        return string
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
