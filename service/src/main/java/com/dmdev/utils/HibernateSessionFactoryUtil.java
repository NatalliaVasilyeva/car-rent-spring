package com.dmdev.utils;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Order;
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
                .addProperties(PropertiesUtil.getProperties())
                .addAnnotatedClass(Accident.class)
                .addAnnotatedClass(Brand.class)
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(CarRentalTime.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(DriverLicense.class)
                .addAnnotatedClass(Model.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserDetails.class)
                .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        return configuration.buildSessionFactory();
    }
}