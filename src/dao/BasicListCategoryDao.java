package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.BasicListCategory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

	private static final class BasicListCategoryMapper implements RowMapper<BasicListCategory> {
	    public BasicListCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	BasicListCategory basicListCategory = new BasicListCategory();
	    	basicListCategory.setCategoryId(rs.getString("CATEGORY_ID"));
	    	basicListCategory.setVersionId(rs.getString("VERSION_ID"));
	    	basicListCategory.setCategoryFatherId(rs.getString("CATEGORY_FATHERID"));
	    	basicListCategory.setCategoryName(rs.getString("CATEGORY_NAME"));
	        return basicListCategory;
	    }
	}
}
