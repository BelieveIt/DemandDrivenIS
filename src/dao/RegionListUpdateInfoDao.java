package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.RegionListUpdateInfo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class RegionListUpdateInfoDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	public RegionListUpdateInfoDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REGION_LIST_UPDATE_INFO");
	}

	public List<RegionListUpdateInfo> queryRegionListUpdateInfos(){
		String sql = "select * from REGION_LIST_UPDATE_INFO order by CREATE_TIME DESC";
		return jdbcTemplate.query(sql, new RegionListUpdateInfoMapper());
	}

	public void insertRegionListUpdateInfo(RegionListUpdateInfo regionListUpdateInfo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REGION_ID", regionListUpdateInfo.getRegionId());
		parameters.put("VERSION_ID", regionListUpdateInfo.getVerionId());
		parameters.put("IS_FINISHED", regionListUpdateInfo.getIsFinished());
		parameters.put("CREATE_TIME", regionListUpdateInfo.getCreateTime());
		parameters.put("FINISHED_TIME", regionListUpdateInfo.getFinishedTime());
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from REGION_LIST_UPDATE_INFO";
		return jdbcTemplate.update(sql);
	}

	private static final class RegionListUpdateInfoMapper implements RowMapper<RegionListUpdateInfo> {
	    public RegionListUpdateInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	RegionListUpdateInfo regionListUpdateInfo = new RegionListUpdateInfo();
	    	regionListUpdateInfo.setRegionId(rs.getString("REGION_ID"));
	    	regionListUpdateInfo.setVerionId(rs.getString("VERSION_ID"));
	    	regionListUpdateInfo.setIsFinished(rs.getInt("IS_FINISHED"));
	    	regionListUpdateInfo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	regionListUpdateInfo.setFinishedTime(rs.getTimestamp("FINISHED_TIME"));	    	
	        return regionListUpdateInfo;
	    }
	}
}
