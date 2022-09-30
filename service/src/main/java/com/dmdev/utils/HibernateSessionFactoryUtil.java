package com.dmdev.utils;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.Price;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateSessionFactoryUtil {

    @SneakyThrows
    public static SessionFactory buildSessionAnnotationFactory() {
        Configuration configuration = new Configuration();

        configuration
                .addProperties(PropertiesUtil.getProperties());

        configuration.addAnnotatedClass(Accident.class);
        configuration.addAnnotatedClass(Brand.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(CarRentalTime.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(DriverLicense.class);
        configuration.addAnnotatedClass(Model.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Price.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(UserDetails.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        return configuration.buildSessionFactory();
    }
}