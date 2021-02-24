package databasetest;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {
    
    public JSONArray getJSONData(){
        
        
        JSONArray jsArr = new JSONArray();
            
        Connection cnx = null;
        PreparedStatement selectStmt = null, updateStmt = null;
        ResultSet rs = null;
        ResultSetMetaData metadata = null;
        
        String query;
         
        boolean hasRez;
        int rezCount, colCount;
        
        try {
            
           
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "";
            System.out.println("Connecting to " + server + "...");
            
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            cnx = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (cnx.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                selectStmt = cnx.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasRez = selectStmt.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( hasRez || selectStmt.getUpdateCount() != -1 ) {

                    if ( hasRez ) {
                        
                        /* Get ResultSet Metadata */
                        
                        rs = selectStmt.getResultSet();
                        metadata = rs.getMetaData();
                        colCount = metadata.getColumnCount();
                        
                        /* Get Data; Print as Table Rows */
                        int currentLine = 0;
                        while(rs.next()) {
                            JSONObject currentJSONObject = new JSONObject();
                            for (int i = 2; i <= colCount; i++){
                                currentJSONObject.put(metadata.getColumnLabel(i), rs.getString(i));
                            }
                            jsArr.add(currentJSONObject);
                        }
                        
                    }

                    else {

                        rezCount = selectStmt.getUpdateCount();  

                        if ( rezCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasRez = selectStmt.getMoreResults();

                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            cnx.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (rs != null) { try { rs.close(); rs = null; } catch (Exception e) {} }
            
            if (selectStmt != null) { try { selectStmt.close(); selectStmt = null; } catch (Exception e) {} }
            
            if (updateStmt != null) { try { updateStmt.close(); updateStmt = null; } catch (Exception e) {} }
            
        }

        return jsArr;
    }
    
}