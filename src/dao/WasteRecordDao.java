package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import model.SellingList;
import model.WasteReason;
import model.WasteRecord;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class WasteRecordDao implements Serializable{
private static final long serialVersionUID = -4665445468041730100L;
private JdbcTemplate jdbcTemplate;
private SimpleJdbcInsert simpleJdbcInsert;
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
public WasteRecordDao(){
	jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
	simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
	.withTableName("WASTE_RECORD");
	namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
}
public void insertWasteRecord(WasteRecord wasteRecord){
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("PRODUCT_ID", wasteRecord.getProductId());
	parameters.put("CREATE_TIME", wasteRecord.getCreateTime());
	parameters.put("WASTE_NUMBER", wasteRecord.getWasteNumber());
	parameters.put("REASON_ID", wasteRecord.getWasteReason().getReasonId());
	parameters.put("STORE_ID", wasteRecord.getStoreId());
	simpleJdbcInsert.execute(parameters);
}
private static final class WasteRecordMapper implements RowMapper<WasteRecord> {
    public WasteRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
    	WasteRecord wasteRecord = new WasteRecord();
    	wasteRecord.setStoreId(rs.getString("STORE_ID"));
    	wasteRecord.setProductId(rs.getString("PRODUCT_ID"));
    	wasteRecord.setWasteNumber(rs.getInt("WASTE_NUMBER"));
    	WasteReason wasteReason = new WasteReason();
    	wasteReason.setReasonId(rs.getString("REASON_ID"));
    	wasteReason.setReasonDescription(rs.getString("REASON_DESCRIPTION"));
    	wasteRecord.setWasteReason(wasteReason);
    	wasteRecord.setCreateTime(rs.getTimestamp("CREATE_TIME"));
        return wasteRecord;
    }
}
}
