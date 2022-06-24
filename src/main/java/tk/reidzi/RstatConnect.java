package tk.reidzi;


import java.sql.*;
import java.util.ArrayList;


public class RstatConnect {

    public static ArrayList<String> getSensorsToday() {
        ArrayList<String> result = new ArrayList<>();
        System.out.println("start");
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:ucanaccess://" + Main.dbpath);

            String str = "SELECT Name, Sum(SumRealIn) AS SumIn, Sum(SumRealOut) AS SumOut FROM (SELECT Shops.Name, Traffic.DateCreated, Sum(Traffic.RealIn) AS SumRealIn, Sum(Traffic.RealOut) AS SumRealOut FROM ((Shops INNER JOIN Entries ON Shops.ID = Entries.ShopID) INNER JOIN Sensors ON Entries.ID = Sensors.EntryID) INNER JOIN Traffic ON Sensors.ID = Traffic.SensorID GROUP BY Shops.Name, Traffic.DateCreated HAVING (((Traffic.DateCreated) Between #5/29/2022# And #5/30/2022#))) GROUP BY Name;";

            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(str);
            System.out.println("comp");
            while (rs.next()) {
                result.add(rs.getString(1));
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}




//        try (Connection connection = DriverManager.getConnection(databaseURL)) {
//                String sql = "SELECT ID, NAME FROM Entries";
//                Statement statement = connection.createStatement();
//                ResultSet result = statement.executeQuery(sql);
//            while (result.next()) {
//                String name = result.getString("ID");
//                String sumIn = result.getString("NAME");
//
//                System.out.println(", " + name + ", " + sumIn + ", ");
//            }
//            String sql = "SELECT Name, Sum(SumRealIn) AS SumIn, Sum(SumRealOut) AS SumOut \n" +
//                    "FROM (\n" +
//                    "SELECT Shops.Name, Traffic.DateCreated, Sum(Traffic.RealIn) AS SumRealIn, Sum(Traffic.RealOut) AS SumRealOut\n" +
//                    "FROM ((Shops INNER JOIN Entries ON Shops.ID = Entries.ShopID) INNER JOIN Sensors ON Entries.ID = Sensors.EntryID) INNER JOIN Traffic ON Sensors.ID = Traffic.SensorID\n" +
//                    "GROUP BY Shops.Name, Traffic.DateCreated\n" +
//                   "HAVING (((Traffic.DateCreated) Between #5/29/2022# And #5/30/2022#))\n" +
//                    ")\n" +
//                    "GROUP BY Name;";
//
//            Statement statement = connection.createStatement();
//            ResultSet result = statement.executeQuery(sql);
//
//            while (result.next()) {
//                String name = result.getString("Name");
//                String sumIn = result.getString("SumIn");
//                String sumOut = result.getString("SumOut");
//
//                System.out.println(", " + name + ", " + sumIn + ", " + sumOut);
//            }

//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }