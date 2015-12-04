package entity;


import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;


public class TestStudent {

	@Test
	public void testSchemaExport() {
		
		//创建配置对象。
		Configuration config = new Configuration().configure();
		//创建服务注册对象。
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
			.applySettings(config.getProperties()).buildServiceRegistry();
		//创建SchemaExport对象。
		SchemaExport export = new SchemaExport(config);
		
		//第一个true：生成表结构，第二个true：输出Sql语句。
		export.create(true, true);
		
	}
}
