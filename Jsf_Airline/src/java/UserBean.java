/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AHMET
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Muhammet
 */
@ManagedBean(name = "User")
@SessionScoped

public class UserBean {

    public String id;
    public String email;
    public String password;
    public String first_name;
    public String last_name;

    public String takeoff_location;
    public String landing_location;
    public String takeoff_date;
    public String date;
    public String hour;
   
    public int flight_ıd;

    public String plane_name;

    public int getFlights_ıd() {
        return flight_ıd;
    }

    public String getPlane_name() {
        return plane_name;
    }

    public void setFlights_ıd(int flights_ıd) {
        this.flight_ıd = flights_ıd;
    }

    public void setPlane_name(String plane_name) {
        this.plane_name = plane_name;
    }

    CachedRowSet rowSet = null;
    DataSource dataSource;

    public UserBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/sample");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String login() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to insert a new address book entry
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT Users.ID,Users.PASSWORD FROM USERS WHERE USERS.ID = " + getId() + " AND USERS.PASSWORD = '" + getPassword() + "'\n");
            if (rs.next()) {
                return "index.xhtml";
            } else {
                return "login.xhml";
            }
//return rowSet;
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }

    public String register() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to insert a new book entry
            PreparedStatement addEntry
                    = connection.prepareStatement("INSERT INTO USERS"
                            + "(ID,EMAIL,PASSWORD,FIRST_NAME,LAST_NAME)"
                            + "VALUES ( ?, ?, ?, ?,? )");

            // specify the PreparedStatement's arguments
            addEntry.setString(1, getId());
            addEntry.setString(2, getEmail());
            addEntry.setString(3, getPassword());
            addEntry.setString(4, getFirst_name());
            addEntry.setString(5, getLast_name());

            addEntry.executeUpdate(); // insert the entry
            return "index"; // go back to index.xhtml home page
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }
////////////////////////////////////////
// index
////////////////////////////////////////

    public ResultSet showName() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        try {
            PreparedStatement ps
                    = connection.prepareStatement("SELECT USERS.FIRST_NAME,USERS.LAST_NAME FROM USERS WHERE USERS.ID=" + getId());
            rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(ps.executeQuery());
            return rowSet;
        } finally {
            connection.close();
        }
    }

///////////////////////////////////////////////////////// 
////////// profile
//////////////////////////////////////////////////////////
    public ResultSet showProfile() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        try {
            PreparedStatement ps
                    = connection.prepareStatement("SELECT USERS.FIRST_NAME,USERS.LAST_NAME,USERS.EMAIL "
                            + "FROM APP.USERS WHERE USERS.ıd=?");
            ps.setString(1, getId());
            rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(ps.executeQuery());
            return rowSet;
        } finally {
            connection.close();
        }
    }

    public String goEditProfile() {
        return "editprofile.xhtml";
    }
//////////////////////////////////////////////////
//// current tickets 

    public ResultSet currentTickets() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to insert a new address book entry
            PreparedStatement ps
                    = connection.prepareStatement("SELECT \n"
                            + "    USERS.FIRST_NAME,\n"
                            + "    USERS.LAST_NAME,\n"
                            + "    FLIGHTS.TAKEOFF_DATE, \n"
                            + "    FLIGHTS.TAKEOFF_LOCATION,\n"
                            + "    FLIGHTS.LANDING_LOCATION,\n"
                            + "    PLANES.NAME\n"
                            + "FROM USERS\n"
                            + "JOIN USER_TICKETS ON USER_TICKETS.USER_ID = USERS.ID\n"
                            + "JOIN TICKETS ON USER_TICKETS.TICKET_ID = TICKETS.ID\n"
                            + "JOIN FLIGHTS ON FLIGHTS.ID = TICKETS.FLIGHT_ID\n"
                            + "JOIN PLANES ON PLANES.ID = FLIGHTS.PLANE_ID\n"
                            + "WHERE USERS.ID =?");

            ps.setString(1, getId());
            rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(ps.executeQuery());
            return rowSet;
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }

//////////////////////////////////////////////////////
///// canncel ticket
//////////////////////
public String cancelTicket() throws SQLException
 {
 // check whether dataSource was injected by the server
 if ( dataSource == null ){ 
      throw new SQLException( "Unable to obtain DataSource" );
 }

 // obtain a connection from the connection pool
 Connection connection = dataSource.getConnection();

 // check whether connection was successful
 if ( connection == null )
 throw new SQLException( "Unable to connect to DataSource" );

 try
 {
 // create a PreparedStatement to insert a new address book entry
 PreparedStatement myObject =
 connection.prepareStatement( "DELETE FROM USER_TICKETS WHERE USER_TICKETS.USER_ID=? ");
 myObject.setString(1, getId());
 // specify the PreparedStatement's arguments
myObject.executeUpdate(); // insert the entry
 return "profile"; // go back to index.xhtml page
 } // end try
 finally
 {
 connection.close(); // return this connection to pool
 } // end finally
 } 

/////////////////////////////////////////////////////
/// edit profile
/////////////////////////////////////////////////
    public String saveProfile() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {

            // create a PreparedStatement to insert a new address book entry
            PreparedStatement addEntry
                    = connection.prepareStatement("UPDATE USERS SET FIRST_NAME=?,LAST_NAME=?,EMAIL=?"
                            + "WHERE USERS.ID=?");

            // specify the PreparedStatement's arguments
            addEntry.setString(4, getId());
            addEntry.setString(1, getFirst_name());
            addEntry.setString(2, getLast_name());
            addEntry.setString(3, getEmail());
            // addEntry.setString(4,getId());
            addEntry.executeUpdate(); // insert the entry
            return "profile";
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }

///////////////////////////////////////////////////////
/// tickets 
////////////////////////////////////////////////////////
    public int temp;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
   
    public String addTicket() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
       
       
        try {
            String sql_satiri="SELECT TICKETS.ID FROM \n" +
                                "TICKETS INNER JOIN FLIGHTS ON FLIGHTS.ID=TICKETS.FLIGHT_ID\n" +
                                "WHERE FLIGHTS.TAKEOFF_LOCATION=? AND FLIGHTS.LANDING_LOCATION=?";
                
              PreparedStatement sql = connection.prepareStatement(sql_satiri);
               sql.setString(2, getLanding_location());
                sql.setString(1, getTakeoff_location());
              ResultSet result=sql.executeQuery();

                while (result.next())  {
            
                 temp = result.getInt("ID");
                
            } 
            
            // create a PreparedStatement to insert a new address book entry
            PreparedStatement myObject
                    = connection.prepareStatement("INSERT INTO USER_TICKETS VALUES (?,?)");
            myObject.setInt(1, getTemp());
            myObject.setString(2, getId());
            // specify the PreparedStatement's arguments
            myObject.executeUpdate(); // insert the entry
            return "profile";
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }

    public ResultSet showAllTickets() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {

            // create a PreparedStatement to insert a new address book entry
            PreparedStatement ps
                    = connection.prepareStatement("SELECT  FLIGHTS.TAKEOFF_DATE, FLIGHTS.TAKEOFF_LOCATION, FLIGHTS.LANDING_LOCATION, TICKETS.PRICE, PLANES.NAME\n"
                            + "FROM\n"
                            + "FLIGHTS INNER JOIN PLANES ON FLIGHTS.PLANE_ID=PLANES.ID\n"
                            + "INNER JOIN TICKETS ON TICKETS.FLIGHT_ID = FLIGHTS.ID\n"
                            + "WHERE FLIGHTS.LANDING_LOCATION=? AND FLIGHTS.TAKEOFF_LOCATION=?"
                    );
            ps.setString(1, getLanding_location());
            ps.setString(2, getTakeoff_location());
            rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(ps.executeQuery());
            return rowSet;
        } // end try
        finally {
            connection.close(); // return this connection to pool
        } // end finally
    }

////////////////////////////////////////////////////////////
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setTakeoff_location(String takeoff_location) {
        this.takeoff_location = takeoff_location;

    }

    public String getTakeoff_location() {
        return takeoff_location;
    }

    public String getLanding_location() {
        return landing_location;
    }

    public String getTakeoff_date() {
        return takeoff_date;
    }

    public void setLanding_location(String landing_location) {
        this.landing_location = landing_location;
    }

    public void setTakeoff_date(String takeoff_date) {
        this.takeoff_date = takeoff_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
