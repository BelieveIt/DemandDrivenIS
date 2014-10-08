package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.BasicListCategory;
import model.Category;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class BasicListCategoryDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public BasicListCategoryDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("BASIC_LIST_CATEGORIES");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public List<BasicListCategory> queryCategoriesByVersionId(String versionId){
		String sql = "select * from BASIC_LIST_CATEGORIES where VERSION_ID = :versionId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("versionId", versionId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new BasicListCategoryMapper());
	}

	public void insertCategory(BasicListCategory basicListCategory){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("CATEGORY_ID", basicListCategory.getCategoryId());
		parameters.put("VERSION_ID", basicListCategory.getVersionId());
		parameters.put("CATEGORY_FATHERID", basicListCategory.getCategoryFatherId());
		parameters.put("CATEGORY_NAME", basicListCategory.getCategoryName());
		parameters.put("CREATE_TIME", basicListCategory.getCreateTime());
		parameters.put("PRODUCT_TYPE_ID", basicListCategory.getProductTypeId());
		parameters.put("DESCRIPTION", basicListCategory.getDescription());
		simpleJdbcInsert.execute(parameters);
	}

	public void insertCategories(List<BasicListCategory> basicListCategories){
		for(BasicListCategory basicListCategory : basicListCategories){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("CATEGORY_ID", basicListCategory.getCategoryId());
			parameters.put("VERSION_ID", basicListCategory.getVersionId());
			parameters.put("CATEGORY_FATHERID", basicListCategory.getCategoryFatherId());
			parameters.put("CATEGORY_NAME", basicListCategory.getCategoryName());
			parameters.put("CREATE_TIME", basicListCategory.getCreateTime());
			parameters.put("PRODUCT_TYPE_ID", basicListCategory.getProductTypeId());
			parameters.put("DESCRIPTION", basicListCategory.getDescription());
			simpleJdbcInsert.execute(parameters);
		}

	}
	public int deleteAll(){
		String sql = "delete from BASIC_LIST_CATEGORIES";
		return jdbcTemplate.update(sql);
	}

	public void deleteCategory(Category category){
		String sql = "delete from BASIC_LIST_CATEGORIES where CATEGORY_ID = :categoryId and VERSION_ID = :versionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(category);
		namedParameterJdbcTemplate.update(sql, sqlParameterSource);
		return;
	}

	public int updateCategory(BasicListCategory basicListCategory){
		String sql = "update BASIC_LIST_CATEGORIES set " +
				"CATEGORY_FATHERID = :categoryFatherId, CATEGORY_NAME = :categoryName, " +
				"PRODUCT_TYPE_ID = :productTypeId, DESCRIPTION = :description " +
				"where CATEGORY_ID = :categoryId and VERSION_ID = :versionId";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(basicListCategory);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	private static final class BasicListCategoryMapper implements RowMapper<BasicListCategory> {
	    public BasicListCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	BasicListCategory basicListCategory = new BasicListCategory();
	    	basicListCategory.setCategoryId(rs.getString("CATEGORY_ID"));
	    	basicListCategory.setVersionId(rs.getString("VERSION_ID"));
	    	basicListCategory.setCategoryFatherId(rs.getString("CATEGORY_FATHERID"));
	    	basicListCategory.setCategoryName(rs.getString("CATEGORY_NAME"));
	    	basicListCategory.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	    	basicListCategory.setProductTypeId(rs.getString("PRODUCT_TYPE_ID"));
	    	basicListCategory.setDescription(rs.getString("DESCRIPTION"));
	        return basicListCategory;
	    }
	}
}
