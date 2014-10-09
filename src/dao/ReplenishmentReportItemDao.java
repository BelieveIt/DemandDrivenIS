package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Product;
import model.ReplenishmentReportItem;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;
import utils.StringUtil;

public class ReplenishmentReportItemDao implements Serializable{
	private static final long serialVersionUID = 6172703265083611143L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public ReplenishmentReportItemDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REPLENISHMENT_REPORT_ITEM");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public void insertItems(List<ReplenishmentReportItem> list){
		for(ReplenishmentReportItem item : list){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_ID", item.getReportId());
			parameters.put("PRODUCT_ID", item.getProductId());
			parameters.put("REPLENISHMENT_NUMBER", item.getReplenishmentNumber());
			parameters.put("CURRENT_INVENTORY", item.getCurrentInventory());
			parameters.put("WASTE_FORECAST", item.getWasteForecast());
			parameters.put("SALES_FORECAST", item.getSalesForecast());
			parameters.put("MIN_INVENTORY_ON_NEXT_DELIVERY", item.getMinInventoryOnNextDelivery());
			simpleJdbcInsert.execute(parameters);
		}
	}

	public List<ReplenishmentReportItem> queryReplenishmentReportItemsByReportId(String reportId){
		String sql = "select * from REPLENISHMENT_REPORT_ITEM where REPORT_ID = :reportId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("reportId", reportId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new ReplenishmentReportItemMapper());
	}
	public int deleteAll(){
		String sql = "delete from REPLENISHMENT_REPORT_ITEM";
		return jdbcTemplate.update(sql);
	}
	private static final class ReplenishmentReportItemMapper implements RowMapper<ReplenishmentReportItem> {
	    public ReplenishmentReportItem mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ReplenishmentReportItem replenishmentReportItem = new ReplenishmentReportItem();
	    	replenishmentReportItem.setCurrentInventory(rs.getInt("CURRENT_INVENTORY"));
	    	replenishmentReportItem.setMinInventoryOnNextDelivery(rs.getInt("MIN_INVENTORY_ON_NEXT_DELIVERY"));
	    	replenishmentReportItem.setProductId(rs.getString("PRODUCT_ID"));
	    	replenishmentReportItem.setReplenishmentNumber(rs.getInt("REPLENISHMENT_NUMBER"));
	    	replenishmentReportItem.setReportId(rs.getString("REPORT_ID"));
	    	replenishmentReportItem.setSalesForecast(rs.getInt("SALES_FORECAST"));
	    	replenishmentReportItem.setWasteForecast(rs.getInt("WASTE_FORECAST"));
	        return replenishmentReportItem;
	    }
	}
}
