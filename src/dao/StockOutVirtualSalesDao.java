package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.SalesRecord;
import model.StockOutVirtualSales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class StockOutVirtualSalesDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public StockOutVirtualSalesDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("STOCK_OUT_VIRTUAL_SALES");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public HashMap<String, ArrayList<StockOutVirtualSales>> queryStockOutVirtualSales(){
		String sql = "select * from STOCK_OUT_VIRTUAL_SALES order by CREATE_TIME desc";
		List<StockOutVirtualSales> records =  jdbcTemplate.query(sql,new StockOutVirtualSalesMapper());
		HashMap<String, ArrayList<StockOutVirtualSales>> map = new HashMap<String, ArrayList<StockOutVirtualSales>>();

		for(StockOutVirtualSales record : records){
			if(map.containsKey(record.getStoreId())){
				map.get(record.getStoreId()).add(record);
			}else {
				ArrayList<StockOutVirtualSales> newList = new ArrayList<StockOutVirtualSales>();
				newList.add(record);
				map.put(record.getStoreId(), newList);
			}
		}
		return map;
	}
	public int deleteAll(){
		String sql = "delete from STOCK_OUT_VIRTUAL_SALES";
		return jdbcTemplate.update(sql);
	}
	public void insertStockOutVirtualSales(StockOutVirtualSales stockOutVirtualSales){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("STORE_ID", stockOutVirtualSales.getStoreId());
		parameters.put("PRODUCT_ID", stockOutVirtualSales.getProductId());
		parameters.put("CREATE_TIME", stockOutVirtualSales.getCreateTime());
		parameters.put("STOCKOUT_NUMBER", stockOutVirtualSales.getStockoutNumber());
		simpleJdbcInsert.execute(parameters);
	}
	private static final class StockOutVirtualSalesMapper implements RowMapper<StockOutVirtualSales> {
	    public StockOutVirtualSales mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	StockOutVirtualSales stockOutVirtualSales = new StockOutVirtualSales();
	    	stockOutVirtualSales.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	stockOutVirtualSales.setProductId(rs.getString("PRODUCT_ID"));
	    	stockOutVirtualSales.setStockoutNumber(rs.getInt("STOCKOUT_NUMBER"));
	    	stockOutVirtualSales.setStoreId(rs.getString("STORE_ID"));
	        return stockOutVirtualSales;
	    }
	}
}
