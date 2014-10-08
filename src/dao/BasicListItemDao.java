package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.BasicListItem;
import model.Product;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;
import utils.StringUtil;

public class BasicListItemDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public BasicListItemDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("BASIC_LIST_PRODUCTS");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<BasicListItem> queryProductsByVersionId(String versionId){
		String sql = "select * from BASIC_LIST_PRODUCTS where VERSION_ID = :versionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new BasicListItemMapper());
	}

	public void insertProduct(BasicListItem basicListItem){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", basicListItem.getProductId());
		parameters.put("VERSION_ID", basicListItem.getVersionId());
		parameters.put("CATEGORY_ID", basicListItem.getCategoryId());
		Product product = basicListItem.getProduct();
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
		parameters.put("ADDITIONAL_INFORMATION", StringUtil.getStringByList(product.getAdditionalInformation()));
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from BASIC_LIST_PRODUCTS";
		return jdbcTemplate.update(sql);
	}

	public void deleteProduct(BasicListItem basicListItem){
		String sql = "delete from BASIC_LIST_PRODUCTS where PRODUCT_ID = :productId and VERSION_ID = :versionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(basicListItem);
		namedParameterJdbcTemplate.update(sql, sqlParameterSource);
		return;
	}

	public int updateProduct(BasicListItem basicListItem){
		String sql = "update BASIC_LIST_PRODUCTS set " +
				"CATEGORY_ID = :categoryId, NAME = :product.name, " +
				"BRAND = :product.brand, ITEM_WEIGHT = :product.itemWeight, " +
				"MANUFACTURER = :product.manufacturer, IMAGE = :product.image, " +
				"PRICE = :product.price, UNIT = :product.unit, " +
				"DELIVERY_FREQUENCY = :product.deliveryFrequency, MIN_INVENTORY = :product.minInventory, " +
				"PRODUCT_CREATE_TIME = :product.productCreateTime, ADDITIONAL_INFORMATION = :product.additionalInformationString " +
				"where PRODUCT_ID = :productId and VERSION_ID = :versionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(basicListItem);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	private static final class BasicListItemMapper implements RowMapper<BasicListItem> {
	    public BasicListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	BasicListItem basicListItem = new BasicListItem();
	    	basicListItem.setCategoryId(rs.getString("CATEGORY_ID"));
	    	basicListItem.setProductId(rs.getString("PRODUCT_ID"));
	    	basicListItem.setVersionId(rs.getString("VERSION_ID"));
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
	    	product.setAdditionalInformation(StringUtil.getStringsBySplit(rs.getString("ADDITIONAL_INFORMATION")));
	    	basicListItem.setProduct(product);
	        return basicListItem;
	    }
	}
}
