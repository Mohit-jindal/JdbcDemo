package org.koushik.javabrains.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.derby.iapi.sql.Row;
import org.koushik.javabrains.model.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class JdbcDaoImpl {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate ; //= new JdbcTemplate();
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public Circle getCircle(int circleId)
	{
		Connection conn = null;
		
		try
		{
			//String driver = "org.apache.derby.jdbc.ClientDriver";
			//Class.forName(driver).newInstance();
			//conn = DriverManager.getConnection("jdbc:derby://localhost:1527/db");
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from circle where id = ?");
			ps.setInt(1, circleId);
			
			Circle circle = null;
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				circle = new Circle(circleId, rs.getString("name"));
			}
			rs.close();
			ps.close();
			return circle;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		finally 
		{
			try 
			{
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public int getCircleCount()
	{
		String sql = "select count(1) from circle";
		//jdbcTemplate.setDataSource(getDataSource());
		return jdbcTemplate.queryForList(sql).size();
	}
	
	public String getCircleName(int circleId)
	{
		String sql = "select name from circle where id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {circleId}, String.class); 
	}
	
	public Circle getCircleForId(int circleId)
	{
		String sql = "select * from circle where id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {circleId}, new CircleMapper());
	}
	
	public List<Circle> getAllCircles()
	{
		String sql = "select * from circle";
		return jdbcTemplate.query(sql, new CircleMapper());
	}
	
	/*
	 * public void insertCircle(Circle circle) { String sql =
	 * "insert into circle (id, name) values (?,?)"; jdbcTemplate.update(sql, new
	 * Object[] {circle.getId(), circle.getName()}); }
	 */
	
	public void insertCircle(Circle circle)
	{
		String sql = "insert into circle (id, name) values (:id, :name)";
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", circle.getId())
														.addValue("name", circle.getName());
		namedParameterJdbcTemplate.update(sql, namedParameters);
	}
	
	public void createTriangleTable()
	{
		String sql = "create table triangle  (id integer, name varchar(50))";
		jdbcTemplate.execute(sql);
	}
	
	private static final class CircleMapper implements RowMapper<Circle>
	{

		@Override
		public Circle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Circle circle = new Circle();
			circle.setId(resultSet.getInt("ID"));
			circle.setName(resultSet.getString("NAME"));
			return circle;
		}
		
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource); 
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

}
