package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DeliveryReport;
import model.DeliveryReportItem;
import model.Store;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


import utils.DaoUtil;

public class DeliveryReportDao implements Serializable{
	private static final long serialVersionUID = -5374719934632576951L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public DeliveryReportDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("DELIVERY_REPORT");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public void insertDeliverReport(DeliveryReport deliveryReport){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CREATE_TIME", deliveryReport.getCreateTime());
		parameters.put("DELIVERY_TIME", deliveryReport.getDeliveryTime());
		parameters.put("REPORT_ID", deliveryReport.getReportId());
		parameters.put("STORE_ID", deliveryReport.getStoreId());
		parameters.put("UPDATED", DeliveryReport.NOT_UPDATED);
		parameters.put("REQUEST_REPORT_ID", deliveryReport.getRequestReportId());
		simpleJdbcInsert.execute(parameters);

		DeliveryReportItemDao deliveryReportItemDao = new DeliveryReportItemDao();
		deliveryReportItemDao.insertItems(deliveryReport.getDeliveryReportItems());
	}

	public int updateDeliveryReport(DeliveryReport deliveryReport){
		String sql = "update DELIVERY_REPORT set " +
				"CREATE_TIME = :createTime, DELIVERY_TIME = :deliveryTime, " +
				"UPDATED = :updated " +
				"where REPORT_ID = :reportId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(deliveryReport);
		namedParameterJdbcTemplate.update(sql, sqlParameterSource);
		DeliveryReportItemDao itemDao = new DeliveryReportItemDao();
		itemDao.updateItems(deliveryReport.getDeliveryReportItems());
		return 1;
	}

	public List<DeliveryReport> queryDeliveryReportsByStoreId(String storeId){
		String sql = "select * from DELIVERY_REPORT where STORE_ID = :storeId order by CREATE_TIME DESC";
		SqlParameterSource namedParameters = new MapSqlParameterSource("storeId", storeId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new DeliveryReportMapper());
	}

	public List<DeliveryReport> queryDeliveryReportsByRequestId(String requestId){
		String sql = "select * from DELIVERY_REPORT where REQUEST_REPORT_ID = :requestId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("requestId", requestId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new DeliveryReportMapper());
	}
	public int deleteAll(){
		String sql = "delete from DELIVERY_REPORT";
		return jdbcTemplate.update(sql);
	}
	private static final class DeliveryReportMapper implements RowMapper<DeliveryReport> {
	    public DeliveryReport mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DeliveryReport deliveryReport = new DeliveryReport();
	    	deliveryReport.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	deliveryReport.setDeliveryTime(rs.getTimestamp("DELIVERY_TIME"));
	    	deliveryReport.setReportId(rs.getString("REPORT_ID"));
	    	deliveryReport.setStoreId(rs.getString("STORE_ID"));
	    	deliveryReport.setUpdated(rs.getString("UPDATED"));
	    	deliveryReport.setRequestReportId(rs.getString("REQUEST_REPORT_ID"));
	    	StoreDao storeDao = new StoreDao();
	    	Store store = storeDao.queryStoreById(deliveryReport.getStoreId());
	    	String regionId = store.getRegionId();

	    	DeliveryReportItemDao deliveryReportItemDao = new DeliveryReportItemDao();
	    	RegionListItemDao regionListItemDao = new RegionListItemDao();
	    	List<DeliveryReportItem> deliveryReportItems = deliveryReportItemDao.queryDeliveryReportItemsByReportId(deliveryReport.getReportId());
	    	for(DeliveryReportItem item : deliveryReportItems){
	    		item.setRegionListItem(regionListItemDao.queryProductByVersionIdAndRegionId(regionId, "head", item.getProductId()));
	    	}
	    	deliveryReport.setDeliveryReportItems(deliveryReportItems);
	        return deliveryReport;
	    }
	}
}
