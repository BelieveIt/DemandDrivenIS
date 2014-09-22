package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Region;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class RegionDao implements Serializable{
	private static final long serialVersionUID = 2825127256397756877L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public RegionDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REGION");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<Region> queryRegions(){
		String sql = "select * from REGION";
		return jdbcTemplate.query(sql, new RegionMapper());
	}
	public Region queryRegion(String regionId){
		String sql = "select * from REGION where REGION_ID = :regionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("regionId", regionId);
		return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new RegionMapper());
	}
	public void insertRegion(Region region){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REGION_ID", region.getRegionId());
		parameters.put("NAME", region.getName());
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from REGION";
		return jdbcTemplate.update(sql);
	}

	private static final class RegionMapper implements RowMapper<Region> {
	    public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Region region = new Region();
	    	region.setRegionId(rs.getString("REGION_ID"));
	    	region.setName(rs.getString("NAME"));
	        return region;
	    }
	}
}
