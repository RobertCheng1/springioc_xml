package com.company;


import com.company.service.User;
import com.company.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		/**
		 * Spring 开发:
		 * Spring Framework主要包括几个模块：
		 *     支持IoC和AOP的容器；
		 *     支持JDBC和ORM的数据访问模块；
		 *     支持声明式事务的模块；
		 *     支持基于Servlet的MVC开发；
		 *     支持基于Reactive的Web开发；
		 *     以及集成JMS、JavaMail、JMX、缓存等其他模块。
		 * Spring的核心就是提供了一个IoC容器，它可以管理所有轻量级的JavaBean组件，
		 * 提供的底层服务包括组件的生命周期管理、配置和组装服务、AOP支持，以及建立在AOP基础上的声明式事务服务等。
		 *
		 * Spring提供的容器又称为IoC容器，什么是IoC? IoC全称Inversion of Control，直译为控制反转。
		 * 传统的应用程序中，控制权在程序本身，程序的控制流程完全由开发者控制，
		 * 例如：CartServlet创建了 BookService，在创建 BookService 的过程中，又创建了DataSource组件。
		 * 这种模式的缺点是，一个组件如果要使用另一个组件，必须先知道如何正确地创建它。
		 * 在IoC模式下，控制权发生了反转，即从应用程序转移到了IoC容器，所有组件不再由应用程序自己创建和配置，而是由IoC容器负责，===这高度总结IoC===
		 * 这样，应用程序只需要直接使用已经创建好并且配置好的组件。为了能让组件在IoC容器中被“装配”出来，需要某种“注入”机制。
		 * 例如，BookService自己并不会创建DataSource，而是等待外部通过setDataSource()方法来注入一个DataSource：
		 * 		public class BookService {
		 * 		    private DataSource dataSource;
		 *
		 * 		    public void setDataSource(DataSource dataSource) {
		 * 		        this.dataSource = dataSource;
		 * 		    }
		 * 		}
		 * 不直接new一个DataSource，而是注入一个DataSource，因此，IoC又称为依赖注入（DI：Dependency Injection），
		 * 它解决了一个最主要的问题：将组件的创建+配置与组件的使用相分离，并且，由IoC容器负责管理组件的生命周期。
		 * 因为IoC容器要负责实例化所有的组件，因此，有必要告诉容器如何创建组件，以及各组件的依赖关系。一种最简单的配置是通过XML文件来实现，例如：
		 * 		<beans>
		 * 		    <bean id="dataSource" class="HikariDataSource" />
		 * 		    <bean id="bookService" class="BookService">
		 * 		        <property name="dataSource" ref="dataSource" />
		 * 		    </bean>
		 * 		    <bean id="userService" class="UserService">
		 * 		        <property name="dataSource" ref="dataSource" />
		 * 		    </bean>
		 * 		</beans>
		 * 上述XML配置文件指示IoC容器创建3个JavaBean组件，并把id为dataSource的组件通过属性dataSource（即调用setDataSource()方法）
		 * 注入到另外两个组件中。在Spring的IoC容器中，我们把所有组件统称为JavaBean，即配置一个组件就是配置一个Bean。
		 * 依赖注入方式:
		 * 		很多Java类都具有带参数的构造方法，如果我们把BookService改造为通过构造方法注入，那么实现代码如下：
		 * 			public class BookService {
		 * 			    private DataSource dataSource;
		 *    		
		 * 			    public BookService(DataSource dataSource) {
		 * 			        this.dataSource = dataSource;
		 * 			    }
		 * 			}
		 * 		Spring的IoC容器同时支持属性注入和构造方法注入，并允许混合使用。
		 * 无侵入容器:
		 * 		在设计上，Spring的IoC容器是一个高度可扩展的无侵入容器。
		 * 		所谓无侵入，是指应用程序的组件无需实现Spring的特定接口，或者说，组件根本不知道自己在Spring的容器中运行。这种无侵入的设计有以下好处：
		 * 		    应用程序组件既可以在Spring的IoC容器中运行，也可以自己编写代码自行组装配置；
		 * 		    测试的时候并不依赖Spring容器，可单独进行测试，大大提高了开发效率。
		 *
		 * 实操联系：From: Spring开发--IoC容器--装配Bean
		 * 我们需要编写一个特定的application.xml配置文件，告诉Spring的IoC容器应该如何创建并组装Bean：
		 *      <?xml version="1.0" encoding="UTF-8"?>
		 *      <beans xmlns="http://www.springframework.org/schema/beans"
		 *          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 *          xsi:schemaLocation="http://www.springframework.org/schema/beans
		 *              https://www.springframework.org/schema/beans/spring-beans.xsd">
		 *
		 *          <bean id="userService" class="com.itranswarp.learnjava.service.UserService">
		 *              <property name="mailService" ref="mailService" />
		 *          </bean>
		 *
		 *          <bean id="mailService" class="com.itranswarp.learnjava.service.MailService" />
		 *		</beans>
		 * 注意观察上述配置文件，其中与XML Schema相关的部分格式是固定的，我们只关注两个<bean ...>的配置：
		 *		每个<bean ...>都有一个id标识，相当于Bean的唯一ID；
		 *     	在userServiceBean中，通过<property name="..." ref="..." />注入了另一个Bean；
		 *      Bean的顺序不重要，Spring根据依赖关系会自动正确初始化。
		 * 把上述XML配置文件用Java代码写出来，就像这样：
		 * 		UserService userService = new UserService();
		 * 		MailService mailService = new MailService();
		 * 		userService.setMailService(mailService);
		 * 只不过Spring容器是通过读取XML文件后使用反射完成的。
		 * 如果注入的不是Bean，而是boolean、int、String这样的数据类型，则通过value注入，例如，创建一个HikariDataSource：
		 *      <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
		 *          <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/test" />
		 *          <property name="username" value="root" />
		 *          <property name="password" value="password" />
		 *          <property name="maximumPoolSize" value="10" />
		 *          <property name="autoCommit" value="true" />
		 *      </bean>
		 * 最后一步，我们需要创建一个Spring的IoC容器实例，然后加载配置文件，让Spring容器为我们创建并装配好配置文件中指定的所有Bean，这只需要一行代码：
		 * 		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		 * ApplicationContext:
		 * 我们从创建Spring容器的代码：可以看到，Spring(自己加的IoC)容器就是 ApplicationContext，它是一个接口，有很多实现类，
		 * 这里我们选择 ClassPathXmlApplicationContext，表示它会自动从classpath中查找指定的XML配置文件。
		 * Spring还提供另一种IoC容器叫BeanFactory，使用方式和ApplicationContext类似：
		 * BeanFactory和ApplicationContext的区别在于:
		 * BeanFactory的实现是按需创建，即第一次获取Bean时才创建这个Bean，而ApplicationContext会一次性创建所有的Bean。
		 * 实际上，ApplicationContext接口是从BeanFactory接口继承而来的，并且，ApplicationContext提供了一些额外的功能，
		 * 包括国际化支持、事件和通知机制等。通常情况下，我们总是使用ApplicationContext，很少会考虑使用BeanFactory。
		 * ===小结===：
		 * Spring的IoC容器接口是ApplicationContext，并提供了多种实现类；
		 * 通过XML配置文件创建IoC容器时，使用ClassPathXmlApplicationContext；
		 * 持有IoC容器后，通过getBean()方法获取Bean的引用。
		 */
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		System.out.println(user.getName());
	}
}
