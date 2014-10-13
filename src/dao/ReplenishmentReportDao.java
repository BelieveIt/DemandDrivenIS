package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ReplenishmentReport;
import model.ReplenishmentReportItem;
import model.Store;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class ReplenishmentReportDao implements Serializable{
	private static final long serialVersionUID = 6172703265083611143L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public ReplenishmentReportDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REPLENISHMENT_REPORT");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public void insertReplenishmentReport(ReplenishmentReport replenishmentReport){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REPORT_ID", replenishmentReport.getReportId());
		parameters.put("STORE_ID", replenishmentReport.getStoreId());
		parameters.put("CREATE_TIME", replenishmentReport.getCreateTime());
		parameters.put("DELIVERY_TIME", replenishmentReport.getDeliveryTime());
		parameters.put("STATUS", replenishmentReport.getStatus());
		parameters.put("DELIVERY_TYPE", replenishmentReport.getDeliveryType());
		simpleJdbcInsert.execute(parameters);

		ReplenishmentReportItemDao replenishmentReportItemDao = new ReplenishmentReportItemDao();
		replenishmentReportItemDao.insertItems(replenishmentReport.getReplenishmentReportItems());
	}

	public List<ReplenishmentReport> queryReplenishmentReports(){
		String sql = "select * from REPLENISHMENT_REPORT order by CREATE_TIME DESC";
		return jdbcTemplate.query(sql, new ReplenishmentReportMapper());
	}

	public ReplenishmentReport queryReplenishmentReportByReportId(String reportId){
		String sql = "select * from REPLENISHMENT_REPORT where REPORT_ID = :reportId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("reportId", reportId);
		return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new ReplenishmentReportMapper());
	}

	public List<ReplenishmentReport> queryReplenishmentReportsByStoreId(String storeId){
		String sql = "select * from REPLENISHMENT_REPORT where STORE_ID = :storeId order by CREATE_TIME DESC";
		SqlParameterSource namedParameters = new MapSqlParameterSource("storeId", storeId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new ReplenishmentReportMapper());
	}

	public int updateReplenishmentReport(ReplenishmentReport replenishmentReport){
		String sql = "update REPLENISHMENT_REPORT set " +
				"CREATE_TIME = :createTime, DELIVERY_TIME = :deliveryTime, " +
				"STATUS = :status, STORE_ID = :storeId, " +
				"DELIVERY_TYPE = :deliveryType, COMMENT_FROM_REGION = :commentFromRegion " +
				"where REPORT_ID = :reportId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(replenishmentReport);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	public int deleteAll(){
		String sql = "delete from REPLENISHMENT_REPORT";
		return jdbcTemplate.update(sql);
	}
	private static final class ReplenishmentReportMapper implements RowMapper<ReplenishmentReport> {
	    public ReplenishmentReport mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ReplenishmentReport replenishmentReport = new ReplenishmentReport();
	    	replenishmentReport.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	replenishmentReport.setDeliveryTime(rs.getTimestamp("DELIVERY_TIME"));
	    	replenishmentReport.setReportId(rs.getString("REPORT_ID"));
	    	replenishmentReport.setStatus(rs.getString("STATUS"));
	    	replenishmentReport.setStoreId(rs.getString("STORE_ID"));
	    	replenishmentReport.setDeliveryType(rs.getString("DELIVERY_TYPE"));
	    	replenishmentReport.setCommentFromRegion(rs.getString("COMMENT_FROM_REGION"));
	    	StoreDao storeDao = new StoreDao();
	    	Store store = storeDao.queryStoreById(replenishmentReport.getStoreId());
	    	String regionId = store.getRegionId();

	    	ReplenishmentReportItemDao replenishmentReportItemDao = new ReplenishmentReportItemDao();
	    	RegionListItemDao regionListItemDao = new RegionListItemDao();
	    	List<ReplenishmentReportItem> replenishmentReportItems = replenishmentReportItemDao.queryReplenishmentReportItemsByReportId(replenishmentReport.getReportId());
	    	for(ReplenishmentReportItem item : replenishmentReportItems){
	    		item.setRegionListItem(regionListItemDao.queryProductByVersionIdAndRegionId(regionId, "head", item.getProductId()));
	    	}

	    	replenishmentReport.setReplenishmentReportItems(replenishmentReportItems);

	        return replenishmentReport;
	    }
	}
}
