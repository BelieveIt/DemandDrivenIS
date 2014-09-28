package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.SalesRecord;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;


public class SalesRecordDao implements Serializable{
private static final long serialVersionUID = -3409920921009654496L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public SalesRecordDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("SALES_RECORD");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public HashMap<String, ArrayList<SalesRecord>> querySalesRecords(){
		String sql = "select * from SALES_RECORD";
		List<SalesRecord> records =  jdbcTemplate.query(sql,new SalesRecordMapper());
		HashMap<String, ArrayList<SalesRecord>> map = new HashMap<String, ArrayList<SalesRecord>>();

		for(SalesRecord record : records){
			if(map.containsKey(record.getStoreId())){
				map.get(record.getStoreId()).add(record);
			}else {
				ArrayList<SalesRecord> newList = new ArrayList<SalesRecord>();
				newList.add(record);
				map.put(record.getStoreId(), newList);
			}
		}
		return map;
	}


	public void insertProduct(SalesRecord salesRecord){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("STORE_ID", salesRecord.getStoreId());
		parameters.put("PRODUCT_ID", salesRecord.getProductId());
		parameters.put("SALES_NUMBER", salesRecord.getSalesNumber());
		parameters.put("CREATE_TIME", salesRecord.getCreateTime());
		simpleJdbcInsert.execute(parameters);
	}
	private static final class SalesRecordMapper implements RowMapper<SalesRecord> {
	    public SalesRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	SalesRecord salesRecord = new SalesRecord();
	    	salesRecord.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	salesRecord.setProductId(rs.getString("PRODUCT_ID"));
	    	salesRecord.setSalesNumber(rs.getInt("SALES_NUMBER"));
	    	salesRecord.setStoreId(rs.getString("STORE_ID"));
	        return salesRecord;
	    }
	}

}
