package com.ooooo.annotation;

import com.ooooo.entity.Message;
import java.util.LinkedList;
import java.util.List;
import org.springframework.core.ResolvableType;

/**
 * @author leizhijie
 * @since 2021/2/23 18:42
 */
@SuppressWarnings("unchecked")
public class SimpleMulticaster {
	
	public List<ISimpleListener> listeners;
	
	public SimpleMulticaster() {
		this.listeners = new LinkedList<>();
	}
	
	public void addListener(ISimpleListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ISimpleListener listener) {
		listeners.remove(listener);
	}
	
	public void multicast(Object o) {
		Message<?> message = null;
		if (o instanceof Message) {
			message = (Message<?>) o;
		} else {
			message = Message.payload(o);
		}
		
		for (ISimpleListener listener : listeners) {
			doMulticast(listener, message);
		}
	}
	
	private void doMulticast(ISimpleListener listener, Message<?> msg) {
		ResolvableType msgGenericType = ResolvableType.forClass(msg.getData().getClass());
		ResolvableType parameterResolvableType = findParameterResolvableType(listener);
		
		if (parameterResolvableType.getRawClass() == Message.class) {
			ResolvableType[] parameterGenerics = parameterResolvableType.getGenerics();
			if (parameterGenerics.length == 0) {
				listener.invoke(msg);
			} else if (parameterGenerics.length == 1 && parameterGenerics[0].isAssignableFrom(msgGenericType)) {
				listener.invoke(msg);
			}
			// more than one generic type count -> not match
		} else if (parameterResolvableType.isAssignableFrom(msgGenericType)) {
			listener.invoke(msg.getData());
		}
	}
	
	private ResolvableType findParameterResolvableType(ISimpleListener<?> listener) {
		ResolvableType resolvableType = null;
		if (listener instanceof SimpleListenerAdapter) {
			// handle case for SimpleListenerAdapter
			SimpleListenerAdapter adapter = (SimpleListenerAdapter) listener;
			resolvableType = adapter.getParameterResolvableType();
		} else {
			// handle case for ISimpleListener
			ResolvableType curResolvableType = ResolvableType.forClass(listener.getClass());
			for (ResolvableType interfaceClazz : curResolvableType.getInterfaces()) {
				if (interfaceClazz.getRawClass() == ISimpleListener.class) {
					if (interfaceClazz.getGenerics().length == 1) {
						resolvableType = interfaceClazz.getGeneric(0);
					} else {
						resolvableType = ResolvableType.forClass(Object.class);
					}
					break;
				}
			}
		}
		return resolvableType;
	}
	
	
}
