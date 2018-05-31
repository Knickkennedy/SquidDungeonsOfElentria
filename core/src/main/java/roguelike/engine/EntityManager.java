package roguelike.engine;

import roguelike.Components.Component;

import java.util.*;

public class EntityManager
{
	int lowestUnassignedEntityID=0;
	List<Integer> allEntities;
	HashMap<Class<?>, HashMap<Integer, ? extends Component>> componentStores;

	public EntityManager()
	{
		allEntities = new LinkedList<Integer>();
		componentStores = new HashMap<Class<?>, HashMap<Integer,? extends Component>>();
	}

	public <T extends Component> T gc(int entity, Class<T> componentType)
	{
		HashMap<Integer, ? extends Component> store = componentStores.get( componentType );

		if( store == null)
			throw new IllegalArgumentException( "GET FAIL: there are no entities with a Component of class: "+componentType );

		T result = componentType.cast(store.get(entity));
		if( result == null )
			throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class\n   missing: "+componentType );

		return result;
	}

	public <T extends Component> List<T> getAllComponentsOfType( Class<T> componentType )
	{
		HashMap<Integer, ? extends Component> store = componentStores.get( componentType );

		if( store == null )
		{
			return new LinkedList<T>();
		}
		else
		{
			List<T> result = new ArrayList<T>((java.util.Collection<T>)store.values());
			return result;
		}
	}

	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponent(Class<T> componentType )
	{
		HashMap<Integer, ? extends Component> store = componentStores.get( componentType );

		if( store == null)
			return new HashSet<Integer>();

		return store.keySet();
	}

	public <T extends Component> void addComponent( int entity, T component )
	{
		HashMap<Integer, ? extends Component> store = componentStores.get( component.getClass() );

		if( store == null )
		{
			store = new HashMap<Integer, T>();
			componentStores.put(component.getClass(), store );
		}

		((HashMap<Integer, T>)store).put(entity, component);
	}

	public int createEntity()
	{

		int newID = generateNewEntityID();

		if( newID < 0 )
		{
			/**
			 Fatal error...
			 */
			return 0;
		}
		else
		{
			allEntities.add(newID);

			return newID;
		}
	}

	public void killEntity( Integer entity ) // Pass as an object to remove key instead of index
	{
		synchronized( this ) // prevent it generating two entities with same ID at once
		{
			allEntities.remove(entity);
			for( HashMap<Integer, ? extends Component> store : componentStores.values() )
			{
				store.remove(entity);
			}
		}
	}

	public int generateNewEntityID()
	{
		synchronized( this ) // prevent it generating two entities with same ID at once
		{
			if( lowestUnassignedEntityID < Integer.MAX_VALUE )
			{
				return lowestUnassignedEntityID++;
			}
			else
			{
				for( int i=0; i<Integer.MAX_VALUE; i++ )
				{
					if(!allEntities.contains(i) )
						return i;
				}

				throw new Error("ERROR: no available Entity IDs; too many entities!" );
			}
		}
	}
}
