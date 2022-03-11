package me.bantling.micro.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import me.bantling.micro.function.SideEffect;

/**
 * {@code EventRegistry} is a registry of an event type that can have listeners added and removed as needed.
 * Events are sent to the current listeners. It is up to the event type to contain any data needed to handle the event,
 * and any response that may be sent.
 * 
 * The event lifecycle is as follows:
 * - An EventRegistry is created
 * - Zero or more EventListener are registered
 * - A sender sends an event
 * - Each EventListener is called with the event, in registration order
 * - each EventListener can modify the event with some kind of response
 * - The sender can examine the event for the response(s)
 * 
 * In some cases, it may be suffficient to simply invoke the listeners with no event data.
 * Effectively, instead of sending an event to listeners, actions are performed.
 * In this scenario, the {@link EventRegistry.NoEvent} class can be used.
 * It is a non-generic subclass of {@code EventRegistry<Void>} that adds methods with no generic signatures.
 * 
 *  @param <T> the type of event to send
 */
public class EventRegistry<T> {
    /**
     * The list of event listeners to send events to
     */
    private final List<Consumer<T>> listeners;
    
    /**
     * Constructor
     */
    public EventRegistry() {
        this.listeners = new LinkedList<>();
    }
    
    /**
     * Add a listener to send events to
     * 
     * @param listener the listener to add
     */
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove a listener so it no longer sends an event.
     * Note that if the same listener was added multiple times by reference, only the first instance is removed, in order of adding.
     * 
     * @param listener the listener to remove
     */
    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
    
    /**
     * Remove all instances of a listener so events are no longer sent to it.
     * 
     * @param listener the listener to remove all instances of
     */
    public void removeAllListeners(Consumer<T> listener) {
        Collections.removeAll(listeners, listener);
    }
    
    /**
     * Send an event to all listeners
     * 
     * @param event the event to send
     */
    public void sendEvent(final T event) {
        for (final Consumer<T> eventListener : listeners) {
            eventListener.accept(event);
        }
    }
    
    /**
     * NoEvent is an non-generic subclass of {@code EventRegistry<Void>} for cases where no event object is sent.
     * Non-generic methods are added.
     */
    public static class NoEvent extends EventRegistry<Void> {
        /**
         * Constructor
         */
        public NoEvent() {
            super();
        }
        
        /**
         * Add an action to perform
         * 
         * @param action the action to perform
         */
        public void addAction(final SideEffect action) {
            super.listeners.add(new SideEffect.ConsumerAdapter(action));
        }
        
        /**
         * Remove an action so it is no longer performed.
         * Note that if the same action was added multiple times, only the first instance is removed.
         * 
         * @param action the action to remove
         */
        public void removeAction(final SideEffect action) {
            super.listeners.remove(new SideEffect.ConsumerAdapter(action));
        }
        
        /**
         * Remove all instances of an action so they are no longer performed.
         * 
         * @param action the action to remove all instances of
         */
        public void removeAllActions(final SideEffect action) {
            Collections.removeAll(super.listeners, new SideEffect.ConsumerAdapter(action));
        }
        
        /**
         * Perform all actions
         */
        public void perform() {
            for (final Consumer<Void> action : super.listeners) {
                action.accept(null);
            }
        }
    }
    
    /**
     * Delegate is a partial delegate of {@link EventRegistry}, all methods except {@link EventRegistry#sendEvent(Object)}
     * are delegated.
     * 
     * @param <T>
     */
    public static class Delegate<T> {
        protected final EventRegistry<T> eventRegistry;
        
        /**
         * Constructor
         */
        public Delegate() {
            this.eventRegistry = new EventRegistry<>();
        }
        
        /**
         * Constructor
         * 
         * @param eventRegistry a non-null event registry to delegate to
         */
        public Delegate(final EventRegistry<T> eventRegistry) {
            this.eventRegistry = Objects.requireNonNull(eventRegistry, "eventRegistry");
        }

        /**
         * @param listener
         * @see me.bantling.micro.util.EventRegistry#addListener(java.util.function.Consumer)
         */
        public void addListener(Consumer<T> listener) {
            eventRegistry.addListener(listener);
        }

        /**
         * @param listener
         * @see me.bantling.micro.util.EventRegistry#removeListener(java.util.function.Consumer)
         */
        public void removeListener(Consumer<T> listener) {
            eventRegistry.removeListener(listener);
        }

        /**
         * @param listener
         * @see me.bantling.micro.util.EventRegistry#removeAllListeners(java.util.function.Consumer)
         */
        public void removeAllListeners(Consumer<T> listener) {
            eventRegistry.removeAllListeners(listener);
        }
    }
}
