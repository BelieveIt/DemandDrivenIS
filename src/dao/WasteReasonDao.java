package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import model.WasteReason;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class WasteReasonDao implements Serializable{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public WasteReasonDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public LinkedHashMap<String, String> queryWasteRecordsMap(){
		String sql = "select * from WASTE_REASON ";
		List<WasteReason> reasons =  jdbcTemplate.query(sql,new WasteReasonMapper());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for(WasteReason reason : reasons){
			map.put(reason.getReasonId(), reason.getReasonDescription());
		}
		return map;
	}
	
	private static final class WasteReasonMapper implements RowMapper<WasteReason> {
	    public WasteReason mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	WasteReason wasteReason = new WasteReason();
	    	wasteReason.setReasonId(rs.getString("REASON_ID"));
	    	wasteReason.setReasonDescription(rs.getString("REASON_DESCRIPTION"));
	        return wasteReason;
	    }
	}
	
}
