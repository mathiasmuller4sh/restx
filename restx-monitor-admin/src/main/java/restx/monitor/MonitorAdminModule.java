package restx.monitor;

import restx.admin.AdminPage;
import restx.factory.Module;
import restx.factory.Provides;

import javax.inject.Named;

/**
 * User: xavierhanin
 * Date: 4/7/13
 * Time: 2:59 PM
 */
@Module
public class MonitorAdminModule {
    @Provides
        @Named("Monitor")
    public AdminPage getMonitorAdminPage() {
        return new AdminPage("/@/ui/monitor/", "Monitor");
    }
}
