package model;

//import app.UserModuleTest;
import enums.UserRole;
//import fare.FareCalculator;
//import fare.StandardFareCalculator;
//import service.ReportService;
//import service.RouteService;
//import service.StationService;
//import service.TicketService;
//import service.TrainService;
//import service.UserService;

public class Admin extends User{

//	private static final FareCalculator fareCalculator = new StandardFareCalculator();
//	private static final UserModuleTest userModuleTest = new UserModuleTest();
//	private static final ReportService reportService = new ReportService();
//	private static final RouteService routeService = new RouteService();
//	private static final StationService stationService = new StationService();
//	private static final TicketService ticketService = new TicketService(fareCalculator);
//	private static final TrainService trainService = new TrainService();
//	private static final UserService userService = new UserService();
//	

    public Admin() {
        super();
        this.role = UserRole.ADMIN;
    }

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }

    
//may not be needed    
    @Override
    public void showDashboard() {
        System.out.println("======= Admin Dashboard =======");
        System.out.println("Welcome, " + name + "!");
        System.out.println("Options: manage stations, trains, routes, users and reports.");
    }
    

//    //***************************Admin Menu***************************   
//	public void adminMenu() {
//	    boolean loggedIn = true;
//	    System.out.println("Login successful!");
//	    while (loggedIn) {
//	        printAdminMenu();
//	        int admChoice = userModuleTest.readInt("Enter choice: ");
//	        switch (admChoice) {
//	            case 1 -> stationManagement();
//	            case 2 -> trainManagement();
//	            case 3 -> routeManagement();
//	            case 4 -> userService.viewAllUsers();
//	            case 5 -> reportService.generateTicketReport(ticketService.getTickets());
//	            case 0 -> {
//	                System.out.println("Admin logged out.");
//	                loggedIn = false;
//	            }
//	            default -> System.out.println("Invalid choice. Please enter number 0 - 5.");
//	        }
//	        System.out.println();
//	    }
//	}
//
//    private static void printAdminMenu() {
//        System.out.println("=========== ADMIN MODULE MENU ===========");
//        System.out.println("1. Station Management");
//        System.out.println("2. Train Management");
//        System.out.println("3. Route Management");
//        System.out.println("4. Users Management");
//        System.out.println("5. Generate Report");
//        System.out.println("0. Logout");
//        System.out.println("=========================================");
//    }
//    
//    
//    //Station Management
//	private static void stationManagement() {
//	    boolean running = true;
//	    while (running) {
//	        printStationInfo();
//	        int choice = userModuleTest.readInt("Enter choice: ");
//	        switch (choice) {
//	            //case 1 -> stationService.addStation(null);
//		        case 1 -> {
//		            String id = userModuleTest.readString("Enter station ID: ");
//		            String name = userModuleTest.readString("Enter station name: ");
//		            String location = userModuleTest.readString("Enter location: ");
//	
//		            Station station = new Station(id, name, location);
//		            stationService.addStation(station);
//	
//		            System.out.println("Station added successfully.");
//		        }
//	            case 2 -> stationService.viewStations();
//	            //case 3 -> stationService.searchStation(name);
//	            case 3 -> {
//	                String name = userModuleTest.readString("Enter station name: ");
//	                Station station = stationService.searchStation(name);
//
//	                if (station != null) {
//	                    station.displayInfo();
//	                } 
//	                else {
//	                    System.out.println("Station not found.");
//	                }
//	            }
//	            case 0 -> running = false;
//	            default -> System.out.println("Invalid choice. Please enter number 0 - 3.");
//	        }
//	        System.out.println();
//	    }
//	}
//	
//    private static void printStationInfo() {
//        System.out.println("=========== STATION MANAGEMENT ==========");
//        System.out.println("1. Add Station");
//        System.out.println("2. View Stations");
//        System.out.println("3. Search Station");
//        System.out.println("0. Back");
//        System.out.println("=========================================");
//    }
//    
//    
//	//Train Management
//	private static void trainManagement() {
//	    boolean running = true;
//	    while (running) {
//	        printTrainInfo();
//	        int choice = userModuleTest.readInt("Enter choice: ");
//	        switch (choice) {
//	            //case 1 -> trainService.addTrain(null);
//		        case 1 -> {
//	                String trainId = userModuleTest.readString("Enter train ID: ");
//	                String trainName = userModuleTest.readString("Enter train name: ");
//	                int capacity = userModuleTest.readInt("Enter train capacity: ");
//	
//	                Train train = new Train(trainId, trainName, capacity);
//	                if (trainService.addTrain(train))
//	                {
//	                    System.out.println("Train added successfully.");
//	                }
//	                else
//	                {
//	                    System.out.println("Train ID already exists.");
//	                }
//	            }
//
//	            case 2 -> trainService.viewTrains();
//	            case 0 -> running = false;
//	            default -> System.out.println("Invalid choice. Please enter number 0 - 2.");
//	        }
//	        System.out.println();
//	    }
//	}
//	
//    private static void printTrainInfo() {
//        System.out.println("============ TRAIN MANAGEMENT ===========");
//        System.out.println("1. Add Train");
//        System.out.println("2. View Trains");
//        System.out.println("0. Back");
//        System.out.println("=========================================");
//    }
//    
//    
//	//Route Management
//	private static void routeManagement() {
//	    boolean running = true;
//	    while (running) {
//	        printRouteInfo();
//	        int choice = userModuleTest.readInt("Enter choice: ");
//	        switch (choice) {
//	            case 1 -> {
//	                String routeId = userModuleTest.readString("Enter Route ID: ");
//	                String sourceId = userModuleTest.readString("Enter Source Station ID: ");
//	                String destinationId = userModuleTest.readString("Enter Destination Station ID: ");
//	                double distance = userModuleTest.readDouble("Enter Distance (km): ");
//
//	                Station source = stationService.findStationById(sourceId);
//	                Station destination = stationService.findStationById(destinationId);
//
//	                if (source == null) {
//	                    System.out.println("Source station not found.");
//	                    break;
//	                }
//
//	                if (destination == null) {
//	                    System.out.println("Destination station not found.");
//	                    break;
//	                }
//
//	                Route route = new Route(
//	                    routeId,
//	                    source,
//	                    destination,
//	                    distance
//	                );
//
//	                routeService.addRoute(route);
//	            }	            
//	            case 2 -> routeService.viewRoutes();
//	            case 3 -> {
//	                String sourceId = userModuleTest.readString("Enter Source Station ID: ");
//	                String destinationId = userModuleTest.readString("Enter Destination Station ID: ");
//
//	                Station source = stationService.findStationById(sourceId);
//	                Station destination = stationService.findStationById(destinationId);
//
//	                if (source == null || destination == null) {
//	                    System.out.println("Station not found.");
//	                    break;
//	                }
//
//	                Route route = routeService.findRoute(source, destination);
//
//	                if (route == null) {
//	                    System.out.println("Route not found.");
//	                } else {
//	                    System.out.println("Route found:");
//	                    route.displayRoute();
//	                }
//	            }
//	            case 0 -> running = false;
//	            default -> System.out.println("Invalid choice. Please enter number 0 - 3.");
//	        }
//	        System.out.println();
//	    }
//	}
//	
//    private static void printRouteInfo() {
//        System.out.println("============ ROUTE MANAGEMENT ===========");
//        System.out.println("1. Add Route");
//        System.out.println("2. View Routes");
//        System.out.println("3. Search Route");
//        System.out.println("0. Back");
//        System.out.println("=========================================");
//    }
//    
}
