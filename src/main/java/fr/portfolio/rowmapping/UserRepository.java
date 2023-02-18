package fr.portfolio.rowmapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public final static RowMapper<User> userMapper =  BeanPropertyRowMapper.newInstance(User.class);
	public final static RowMapper<Role> roleMapper =  BeanPropertyRowMapper.newInstance(Role.class);
	
	    
	public User findByUser(String username)
	{
		String sql = "SELECT u.*, r.id_rle, r.role_rle FROM utilisateur AS u "                          + 
				     "INNER JOIN utilisateur_role                       AS ur ON u.id_utl = ur.id_utl " +
				     "INNER JOIN role                                   AS r  ON r.id_rle = ur.id_rle " +
				     "WHERE name_utl =?"                                                                ;
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<User>() 
		{
	        @Override
    		public User extractData(ResultSet rs) throws SQLException, DataAccessException 
    		{
    	        User user = null;
    	        Role role = null;
		        int row = 0;
		        while(rs.next()) 
		        {
		            if(user == null) 
		            {
		                user = userMapper.mapRow(rs, row);
		                user.setId(rs.getInt("id_utl"));
		                user.setUsername(rs.getString("name_utl"));
		        		user.setPassword(rs.getString("pass_utl"));
		            }
		            role = roleMapper.mapRow(rs, row);
		            role.setId(rs.getInt("id_rle"));
		            role.setName(rs.getString("role_rle"));
		            user.addRole(role);
		            row++;
		        }   
		        return user;
    		}   
    		
        }, username);
    }
	
	
	public List<User> findAllUsers()
	{
		String sql = "SELECT u.*, r.id_rle, r.role_rle FROM utilisateur AS u "                          + 
				     "INNER JOIN utilisateur_role                       AS ur ON u.id_utl = ur.id_utl " +
				     "INNER JOIN role                                   AS r  ON r.id_rle = ur.id_rle " ;
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<User>>()
		{
	        @Override
    		public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException
    		{
	        	List<User> users = new ArrayList<User>();
    	        User user        = null;
    	        Role role        = null;
		        int  row         =  0;
		        int  id          = -1;
		        while(rs.next()) 
		        {
		        	if(user == null || id != rs.getInt("id_utl")) 
		            {
		        		id   = rs.getInt("id_utl");
		                user = userMapper.mapRow(rs, row);
		                user.setId(rs.getInt("id_utl"));
		                user.setUsername(rs.getString("name_utl"));
		        		user.setPassword(rs.getString("pass_utl"));
		        		users.add(user);
		            }
		            role = roleMapper.mapRow(rs, row);
		            role.setId(rs.getInt("id_rle"));
		            role.setName(rs.getString("role_rle"));
		            user.addRole(role);
		            row++;
		        }   
		        return users;
    		}   
        });
    }
}