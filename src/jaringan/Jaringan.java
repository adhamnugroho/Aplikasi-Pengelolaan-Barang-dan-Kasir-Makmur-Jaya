package jaringan;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import com.mysql.jdbc.Driver;

public class Jaringan {
    
    public Connection connect = null;
    
    
    public static Connection config() {
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/makmurjaya", "root", "");
            return connect;
        } catch(Exception e) {
            
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
