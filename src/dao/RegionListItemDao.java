package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Product;
import model.RegionListItem;

import oracle.net.aso.r;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class RegionListItemDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public RegionListItemDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REGION_LIST_PRODUCTS");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<RegionListItem> queryProductsByVersionId(String versionId){
		String sql = "select * from REGION_LIST_PRODUCTS where VERSION_ID = :versionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new RegionListItemMapper());
	}
	public List<RegionListItem> queryProductsByVersionIdAndRegionId(String regionId, String versionId){
		String sql = "select * from REGION_LIST_PRODUCTS where REGION_ID = :regionId and VERSION_ID = :versionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId).addValue("regionId", regionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new RegionListItemMapper());
	}
	public void insertProduct(RegionListItem regionListItem){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", regionListItem.getProductId());
		parameters.put("VERSION_ID", regionListItem.getVersionId());
		parameters.put("CATEGORY_ID", regionListItem.getCategoryId());
		parameters.put("IS_CONFIRMED", regionListItem.getIsConfirmed());
		parameters.put("IS_REGION_ADD", regionListItem.getIsRegionAdd());
		parameters.put("REGION_ID", regionListItem.getRegionId());
		Product product = regionListItem.getProduct();
		parameters.put("NAME", product.getName());
		parameters.put("BRAND", product.getBrand());
		parameters.put("ITEM_WEIGHT", product.getItemWeight());
		parameters.put("MANUFACTURER", product.getManufacturer());
		parameters.put("IMAGE", product.getImage());
		parameters.put("PRICE", product.getPrice());
		parameters.put("UNIT", product.getUnit());
		parameters.put("DELIVERY_FREQUENCY", product.getDeliveryFrequency());
		parameters.put("MIN_INVENTORY", product.getMinInventory());
		parameters.put("PRODUCT_CREATE_TIME", product.getProductCreateTime());
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from REGION_LIST_PRODUCTS";
		return jdbcTemplate.update(sql);
	}

	public void deleteProduct(RegionListItem regionListItem){
		String sql = "delete from REGION_LIST_PRODUCTS where PRODUCT_ID = :productId and VERSION_ID = :versionId and REGION_ID = :regionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(regionListItem);
		namedParameterJdbcTemplate.update(sql, sqlParameterSource);
		return;
	}

	public int updateProduct(RegionListItem regionListItem){
		String sql = "update REGION_LIST_PRODUCTS set " +
				"IS_CONFIRMED = :isConfirmed, IS_REGION_ADD = :isRegionAdd, " +
				"CATEGORY_ID = :categoryId, NAME = :product.name, " +
				"BRAND = :product.brand, ITEM_WEIGHT = :product.itemWeight, " +
				"MANUFACTURER = :product.manufacturer, IMAGE = :product.image, " +
				"PRICE = :product.price, UNIT = :product.unit, " +
				"DELIVERY_FREQUENCY = :product.deliveryFrequency, MIN_INVENTORY = :product.minInventory, " +
				"PRODUCT_CREATE_TIME = :product.productCreateTime " +
				"where PRODUCT_ID = :productId and VERSION_ID = :versionId and REGION_ID = :regionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(regionListItem);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	private static final class RegionListItemMapper implements RowMapper<RegionListItem> {
	    public RegionListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	RegionListItem regionListItem = new RegionListItem();
	    	regionListItem.setCategoryId(rs.getString("CATEGORY_ID"));
	    	regionListItem.setProductId(rs.getString("PRODUCT_ID"));
	    	regionListItem.setVersionId(rs.getString("VERSION_ID"));
	    	regionListItem.setRegionId(rs.getString("REGION_ID"));
	    	regionListItem.setIsConfirmed(rs.getInt("IS_CONFIRMED"));
	    	regionListItem.setIsRegionAdd(rs.getInt("IS_REGION_ADD"));
	    	Product product = new Product();
	    	product.setName(rs.getString("NAME"));
	    	product.setBrand(rs.getString("BRAND"));
	    	product.setItemWeight(rs.getInt("ITEM_WEIGHT"));
	    	product.setManufacturer(rs.getString("MANUFACTURER"));
	    	product.setImage(rs.getString("IMAGE"));
	    	product.setPrice(rs.getBigDecimal("PRICE"));
	    	product.setUnit(rs.getString("UNIT"));
	    	product.setDeliveryFrequency(rs.getString("DELIVERY_FREQUENCY"));
	    	product.setMinInventory(rs.getString("MIN_INVENTORY"));
	    	product.setProductCreateTime(rs.getTimestamp("PRODUCT_CREATE_TIME"));

	    	regionListItem.setProduct(product);
	        return regionListItem;
	    }
	}
}
