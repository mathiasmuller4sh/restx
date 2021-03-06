package restx.mongo;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import restx.factory.AutoStartable;
import restx.factory.Module;
import restx.factory.Provides;

import javax.inject.Named;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Module providing some sensible defaults for mongo client.
 *
 * Customize by overriding some or all of these components.
 *
 * Eg:
 * -Dmongo.host=dharma.mongohq.com -Dmongo.port=10002 -Dmongo.user=rxinvoice -Dmongo.password=XXXXXXXXX
 */
@Module(priority = 1000)
public class MongoModule {
    public static final String MONGO_CLIENT_NAME = "mongoClient";
    public static final String MONGO_DB_NAME = "mongo.db";
    public static final String MONGO_URI = "mongo.uri";

    @Provides @Named(MONGO_CLIENT_NAME)
    public MongoClient mongoClient(MongoSettings settings) {
        try {
            return new MongoClient(new MongoClientURI(settings.uri()));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    public AutoStartable mongoHealthChecks(final HealthCheckRegistry registry, final MongoClient mongo) {
        return new AutoStartable() {
            @Override
            public void start() {
                registry.register("mongo", new MongoHealthCheck(mongo));
            }
        };
    }
}
