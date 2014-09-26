package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.RegionListItem;
import model.RelationSellingListAndProduct;
import model.Store;
import model.StoreSellingProduct;

import oracle.net.aso.s;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


import utils.DaoUtil;

public class StoreDao implements Serializable{
private static final long serialVersionUID = 6078373053365007678L;
private SimpleJdbcInsert simpleJdbcInsert;
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
public StoreDao(){
	simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
	.withTableName("STORE");
	namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
}

public List<Store> queryStoresByRegionId(String regionId){
	String sql = "select STORE.*, COMMERCIAL_ENVIRONMENT.NAME as ENVIRONMENT_NAME from STORE " +
			"left outer join COMMERCIAL_ENVIRONMENT " +
			"on STORE.ENVIRONMENT_ID = COMMERCIAL_ENVIRONMENT.ENVIRONMENT_ID " +
			"where REGION_ID = :regionId";
	SqlParameterSource namedParameters = new MapSqlParameterSource("regionId", regionId);
	return namedParameterJdbcTemplate.query(sql, namedParameters, new StoreMapper());
}

public Store queryStoreById(String storeId){
	String sql = "select * from STORE where STORE_ID = :storeId";
	SqlParameterSource namedParameters = new MapSqlParameterSource("storeId", storeId);
	return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new StoreMapper());
}

public List<StoreSellingProduct> queryRegionListItemsByStoreId(String storeId){
	Store store = queryStoreById(storeId);

	String sql = "select * from RELA_STORE_PRODUCT where STORE_ID = : storeId";
	SqlParameterSource namedParameters = new MapSqlParameterSource("storeId", storeId);
	List<StoreSellingProduct> list = namedParameterJdbcTemplate.query(sql, namedParameters, new StoreSellingProductMapper());

	RegionListItemDao regionListItemDao = new RegionListItemDao();
	HashMap<String, RegionListItem> map = regionListItemDao.queryProductsMapByVersionIdAndRegionId(store.getRegionId(), "head");
	for(StoreSellingProduct storeSellingProduct : list){
		if(map.containsKey(storeSellingProduct.getProductId())){
			storeSellingProduct.setIsAvailable(1);
			storeSellingProduct.setRegionListItem(map.get(storeSellingProduct.getProductId()));
		}else {
			storeSellingProduct.setIsAvailable(0);
		}
	}
	return list;
}

public void insertStore(Store store){
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("ADDRESS", store.getAddress());
	parameters.put("CREATE_TIME", store.getCreateTime());
	parameters.put("ENVIRONMENT_ID", store.getEnvironmentId());
	parameters.put("NAME", store.getName());
	parameters.put("OPERATING_AREA", store.getOperatingArea());
	parameters.put("REGION_ID", store.getRegionId());
	parameters.put("STORE_ID", store.getStoreId());
	simpleJdbcInsert.execute(parameters);
}
private static final class StoreMapper implements RowMapper<Store> {
    public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Store store = new Store();
    	store.setAddress(rs.getString("ADDRESS"));
    	store.setCreateTime(rs.getTimestamp("CREATE_TIME"));
    	store.setEnvironmentId(rs.getString("ENVIRONMENT_ID"));
    	store.setName(rs.getString("NAME"));
    	store.setOperatingArea(rs.getInt("OPERATING_AREA"));
    	store.setRegionId(rs.getString("REGION_ID"));
    	store.setStoreId(rs.getString("STORE_ID"));
    	store.setEnvironmentName(rs.getString("ENVIRONMENT_NAME"));
        return store;
    }
}
private static final class StoreSellingProductMapper implements RowMapper<StoreSellingProduct> {
    public StoreSellingProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
    	StoreSellingProduct storeSellingProduct = new StoreSellingProduct();
    	storeSellingProduct.setStoreId(rs.getString("STORE_ID"));
    	storeSellingProduct.setProductId(rs.getString("PRODUCT_ID"));
    	storeSellingProduct.setCurrentInventory(rs.getInt("CURRENT_INVENTORY"));
        return storeSellingProduct;
    }
}
}
