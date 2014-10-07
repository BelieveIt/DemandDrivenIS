package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ProductType;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;



import utils.DaoUtil;
import utils.StringUtil;

public class ProductTypeDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public ProductTypeDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("PRODUCT_TYPE");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	
	public void insertProductType(ProductType productType){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_TYPE_ID", productType.getProductTypeId());
		parameters.put("PRODUCT_TYPE_NAME", productType.getProductTypeName());
		parameters.put("ADDITIONAL_INFORMATION_LABLE", StringUtil.getStringByList(productType.getAdditionalInformationLable()));
		simpleJdbcInsert.execute(parameters);
	}
	
	public List<ProductType> queryProductTypes(){
		String sql = "select * from PRODUCT_TYPE order by PRODUCT_TYPE_NAME ASC";
		return jdbcTemplate.query(sql, new ProductTypeMapper());
	}
	
	public ProductType queryProductType(String productTypeId){
		String sql = "select * from PRODUCT_TYPE where PRODUCT_TYPE_ID = :productTypeId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("productTypeId", productTypeId);
		return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new ProductTypeMapper());
	}
	
	public int updateProductType(ProductType productType){
		String sql = "update PRODUCT_TYPE set " +
				"PRODUCT_TYPE_NAME = :productTypeName, ADDITIONAL_INFORMATION_LABLE = :additionalInformationLable " +
				"where PRODUCT_TYPE_ID = :productTypeId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("productTypeName", productType.getProductTypeName())
		.addValue("additionalInformationLable", StringUtil.getStringByList(productType.getAdditionalInformationLable()))
		.addValue("productTypeId", productType.getProductTypeId());
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}
	
	private static final class ProductTypeMapper implements RowMapper<ProductType> {
	    public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ProductType productType = new ProductType();
	    	productType.setProductTypeId(rs.getString("PRODUCT_TYPE_ID"));
	    	productType.setProductTypeName(rs.getString("PRODUCT_TYPE_NAME"));
	    	String addintionalInfoLabel = rs.getString("ADDITIONAL_INFORMATION_LABLE");
	    	productType.setAdditionalInformationLable(StringUtil.getStringsBySplit(addintionalInfoLabel));
	        return productType;
	    }
	}
}
