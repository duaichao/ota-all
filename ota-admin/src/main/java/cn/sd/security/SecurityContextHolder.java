package cn.sd.security;


public class SecurityContextHolder {
	private static ThreadLocalSecurityContextHolderStrategy strategy;
	private static int initializeCount = 0;
	
	public static void clearContext() {
		strategy.clearContext();
	}

	public static SecurityContext getContext() {
		return strategy.getContext();
	}
	
	public static int getInitializeCount() {
		return initializeCount;
	}
	
	private static void initialize() {
		strategy = new ThreadLocalSecurityContextHolderStrategy();
		initializeCount++;
	}
	
	public static void setContext(SecurityContext context) {
		strategy.setContext(context);
	}
	
	static {
		initialize();
	}
}
