package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.RegionListCategory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class RegionListCategoryDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public RegionListCategoryDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("REGION_LIST_CATEGORIES");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<RegionListCategory> queryCategoriesByVersionId(String versionId, String regionId){
		String sql = "select * from REGION_LIST_CATEGORIES where VERSION_ID = :versionId and REGION_ID = :regionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId).addValue("regionId", regionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new RegionListCategoryMapper());
	}

	public void insertCategory(RegionListCategory regionListCategory){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CATEGORY_ID", regionListCategory.getCategoryId());
		parameters.put("VERSION_ID", regionListCategory.getVersionId());
		parameters.put("REGION_ID", regionListCategory.getRegionId());
		parameters.put("CATEGORY_FATHERID", regionListCategory.getCategoryFatherId());
		parameters.put("CATEGORY_NAME", regionListCategory.getCategoryName());
		parameters.put("CREATE_TIME", regionListCategory.getCreateTime());
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from REGION_LIST_CATEGORIES";
		return jdbcTemplate.update(sql);
	}
	public int deleteAllHeadByRegionId(String regionId, String versionId){
		String sql = "delete from REGION_LIST_CATEGORIES where VERSION_ID = :versionId and REGION_ID = :regionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId).addValue("regionId", regionId);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}
	public int updateCategory(RegionListCategory regionListCategory){
		String sql = "update REGION_LIST_CATEGORIES set CATEGORY_NAME = :categoryName " +
				"where CATEGORY_ID = :categoryId and VERSION_ID = :versionId and REGION_ID = :regionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(regionListCategory);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	private static final class RegionListCategoryMapper implements RowMapper<RegionListCategory> {
	    public RegionListCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	RegionListCategory regionListCategory = new RegionListCategory();
	    	regionListCategory.setCategoryId(rs.getString("CATEGORY_ID"));
	    	regionListCategory.setVersionId(rs.getString("VERSION_ID"));
	    	regionListCategory.setRegionId(rs.getString("REGION_ID"));
	    	regionListCategory.setCategoryFatherId(rs.getString("CATEGORY_FATHERID"));
	    	regionListCategory.setCategoryName(rs.getString("CATEGORY_NAME"));
	    	regionListCategory.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	        return regionListCategory;
	    }
	}
}
