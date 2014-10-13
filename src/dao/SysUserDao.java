package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Region;
import model.Store;
import model.SysUser;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class SysUserDao implements Serializable{
	private static final long serialVersionUID = 7776757938181574097L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public SysUserDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("SYS_USER");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public void insertSysUser(SysUser sysUser){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("USERNAME", sysUser.getUsername());
		parameters.put("PASSWORD", sysUser.getPassword());
		parameters.put("USER_ID", sysUser.getUserId());
		parameters.put("USERTYPE", sysUser.getUsertype());
		simpleJdbcInsert.execute(parameters);
	}
	public SysUser querySysUser(String username, String password){
		try{
			String sql = "select * from SYS_USER where USERNAME = :username and PASSWORD = :password";
			SqlParameterSource namedParameters = new MapSqlParameterSource("username", username)
			.addValue("password", password);
			SysUser sysUser = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new SysUserMapper());
			if(sysUser !=null){
				String userId = sysUser.getUserId();
				String userType = sysUser.getUsertype();
				if(userType.equals(SysUser.FRANCHISER_MERCHANDISE)){
					sysUser.setOperatingUnit("Franchiser Head Office");
					sysUser.setRoleName("Merchandise Info Administrator");
				}
				if(userType.equals(SysUser.REGION_MERCHANDISE)){
					sysUser.setOperatingUnit("Region Head Office");
					sysUser.setRoleName("Merchandise Info Administrator");
					RegionDao regionDao = new RegionDao();
					Region region = regionDao.queryRegionById(userId);
					sysUser.setUserObject(region);
				}

				if(userType.equals(SysUser.REGION_INVENTORY)){
					sysUser.setOperatingUnit("Region Head Office");
					sysUser.setRoleName("Supply Chain Administrator");
					RegionDao regionDao = new RegionDao();
					Region region = regionDao.queryRegionById(userId);
					sysUser.setUserObject(region);
				}
				if(userType.equals(SysUser.STORE_INVENTORY)){
					sysUser.setOperatingUnit("Convenience Store");
					sysUser.setRoleName("Inventory Administrator");
					StoreDao storeDao = new StoreDao();
					Store store = storeDao.queryStoreById(userId);
					sysUser.setUserObject(store);
				}
			}
			return sysUser;
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public int deleteAll(){
		String sql = "delete from SYS_USER";
		return jdbcTemplate.update(sql);
	}
	private static final class SysUserMapper implements RowMapper<SysUser> {
	    public SysUser mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	SysUser sysUser = new SysUser();
	    	sysUser.setUsername(rs.getString("USERNAME"));
	    	sysUser.setPassword(rs.getString("PASSWORD"));
	    	sysUser.setUserId(rs.getString("USER_ID"));
	    	sysUser.setUsertype(rs.getString("USERTYPE"));
	        return sysUser;
	    }
	}
}
