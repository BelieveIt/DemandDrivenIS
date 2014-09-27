package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.RegionListItem;
import model.RelationSellingListAndProduct;
import model.SellingList;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class SellingListDao implements Serializable{
	private static final long serialVersionUID = -5056413551078921450L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public SellingListDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("SELLING_LIST");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<SellingList> querySellingListsByRegionId(String regionId){
		String sql = "select * from SELLING_LIST where REGION_ID = : regionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("regionId", regionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new SellingListMapper());
	}
	public SellingList querySellingListById(String listId){
		String sql = "select * from SELLING_LIST where LIST_ID = : listId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("listId", listId);
		return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new SellingListMapper());
	}
	public List<RegionListItem> queryRegionListItemsBySellingListId(String listId){
		SellingList sellingList = querySellingListById(listId);

		String sql = "select * from RELA_SELLING_LIST_PRODUCT where LIST_ID = : listId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("listId", listId);
		List<RelationSellingListAndProduct> relationSellingListAndProducts = namedParameterJdbcTemplate.query(sql, namedParameters, new RelationSellingListAndProductMapper());

		RegionListItemDao regionListItemDao = new RegionListItemDao();
		HashMap<String, RegionListItem> map = regionListItemDao.queryProductsMapByVersionIdAndRegionId(sellingList.getRegionId(), "head");

		List<RegionListItem> items = new ArrayList<RegionListItem>();
		for(RelationSellingListAndProduct relationSellingListAndProduct : relationSellingListAndProducts){
			items.add(map.get(relationSellingListAndProduct.getProductId()));
		}
		return items;
	}
	public void insertProduct(SellingList sellingList){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("LIST_ID", sellingList.getListId());
		parameters.put("REGION_ID", sellingList.getRegionId());
		parameters.put("CREATE_TIME", sellingList.getCreateTime());
		simpleJdbcInsert.execute(parameters);
	}
	private static final class SellingListMapper implements RowMapper<SellingList> {
	    public SellingList mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	SellingList sellingList = new SellingList();
	    	sellingList.setListId(rs.getString("LIST_ID"));
	    	sellingList.setRegionId(rs.getString("REGION_ID"));
	    	sellingList.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	        return sellingList;
	    }
	}

	private static final class RelationSellingListAndProductMapper implements RowMapper<RelationSellingListAndProduct> {
	    public RelationSellingListAndProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	RelationSellingListAndProduct relationSellingListAndProduct = new RelationSellingListAndProduct();
	    	relationSellingListAndProduct.setListId(rs.getString("LIST_ID"));
	    	relationSellingListAndProduct.setProductId(rs.getString("PRODUCT_ID"));
	        return relationSellingListAndProduct;
	    }
	}
}
