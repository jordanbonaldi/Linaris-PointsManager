package com.theuniverscraft.PointsManager.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.OfflinePlayer;


public class PointsManager {
	private Connection connection;
	private final String BDD_POINTS = YamlConfig.getInstance().getValue("mysql.table");
	private final String PSEUDO_CHAMP = YamlConfig.getInstance().getValue("mysql.champ_pseudo");
	private final String POINTS_CHAMP = YamlConfig.getInstance().getValue("mysql.champ_points");
	
	private static PointsManager instance = null;
	public static PointsManager getInstance() {
		if(instance == null) {
			instance = new PointsManager();
		}
		return instance;
	}
	
	public static void closeInstance() {
		if(instance != null) {
			instance.save();
		}
	}
	
	private PointsManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+YamlConfig.getInstance().getValue("mysql.host")+":"+
					YamlConfig.getInstance().getValue("mysql.port").toString()+
					"/"+YamlConfig.getInstance().getValue("mysql.name");
			String user = YamlConfig.getInstance().getValue("mysql.user");
			String password = YamlConfig.getInstance().getValue("mysql.password");
			
			connection = DriverManager.getConnection(url, user, password);
			Statement state = connection.createStatement();
			
			String sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(BDD_POINTS).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`"+PSEUDO_CHAMP+"` varchar(50) NOT NULL,")
					.append("`"+POINTS_CHAMP+"` int(11) NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`"+PSEUDO_CHAMP+"`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			state.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public int getPoints(OfflinePlayer player) {
		try {
			String sql = "SELECT * FROM "+BDD_POINTS+" WHERE "+PSEUDO_CHAMP+"=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			ResultSet result = state.executeQuery();
			
			if(result.next()) {
				return result.getInt(POINTS_CHAMP);
			}
			state.close();
		} catch(Exception e) { e.printStackTrace(); }
		
		return 0;
	}
	
	public void setPoints(OfflinePlayer player, int points) {
		try {
			String sql = "UPDATE IGNORE "+BDD_POINTS+" SET "+POINTS_CHAMP+"=? WHERE "+PSEUDO_CHAMP+"=?;";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setInt(1, points);
			state.setString(2, player.getName());
			state.executeUpdate();
			
			state.close();
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void addPoints(OfflinePlayer player, int points) {
		try {
			String sql = "UPDATE IGNORE "+BDD_POINTS+" SET "+POINTS_CHAMP+"="+POINTS_CHAMP+"+? WHERE "+PSEUDO_CHAMP+"=?;";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setInt(1, points);
			state.setString(2, player.getName());
			state.executeUpdate();
			
			state.close();
		} catch(Exception e) { e.printStackTrace(); }
	}
	public void subPoints(OfflinePlayer player, int points) {
		addPoints(player, -points);
	}
	
	public void save() {
		try {			
			connection.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
