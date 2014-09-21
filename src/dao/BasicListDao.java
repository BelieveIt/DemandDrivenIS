package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.BasicList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class BasicListDao implements Serializable{
	private static final long serialVersionUID = 7250936813405061101L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	public BasicListDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("BASIC_LIST");
	}

	public List<BasicList> queryBasicLists(){
		String sql = "select * from BASIC_LIST order by CREATE_TIME DESC";
		return jdbcTemplate.query(sql, new BasicListMapper());
	}

	public void insertBasicList(BasicList basicList){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("VERSION_ID", basicList.getVersionId());
		parameters.put("CREATE_TIME", basicList.getCreateTime());
		simpleJdbcInsert.execute(parameters);
	}
	public int deleteAll(){
		String sql = "delete from BASIC_LIST";
		return jdbcTemplate.update(sql);
	}

	private static final class BasicListMapper implements RowMapper<BasicList> {
	    public BasicList mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	BasicList basicList = new BasicList();
	    	basicList.setVersionId(rs.getString("VERSION_ID"));
	    	basicList.setCreateTime(rs.getTimestamp("CREATE_TIME"));
	        return basicList;
	    }
	}
}
