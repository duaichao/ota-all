package cn.sd.security;


@SuppressWarnings("unchecked")
public class ThreadLocalSecurityContextHolderStrategy {
	private static ThreadLocal contextHolder = new ThreadLocal();

	public ThreadLocalSecurityContextHolderStrategy() {
	}

	public void clearContext() {
		contextHolder.set(null);
	}

	public SecurityContext getContext() {
		if (contextHolder.get() == null) {
			contextHolder.set(new SecurityContext());
		}
		return (SecurityContext) contextHolder.get();
	}

	public void setContext(SecurityContext context) {
		contextHolder.set(context);
	}
}
