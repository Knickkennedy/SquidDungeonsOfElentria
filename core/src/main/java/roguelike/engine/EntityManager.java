package roguelike.engine;

import roguelike.Components.Component;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import java.util.*;

@SuppressWarnings("unchecked")
public class EntityManager
{
	private int lowestUnassignedEntityID=0;
	private List<Integer> allEntities;
	private HashMap<Class<?>, HashMap<Integer, Component>> componentStores;
	public SparseLayers display = null;
	public Integer player = 0;
	public EntityManager()
	{
		allEntities = new LinkedList<>();
		componentStores = new HashMap<>();
	}

	public <T extends Component> T gc(int entity, Class<T> componentType)
	{
		HashMap<Integer, Component> store = componentStores.get( componentType );

		if( store == null)
			return null;
			//throw new IllegalArgumentException( "GET FAIL: there are no entities with a Component of class: "+componentType );

		/*if( result == null )
			throw new IllegalArgumentException( "GET FAIL: "+entity+" does not possess Component of class\n   missing: "+componentType );*/

		return componentType.cast(store.get(entity));
	}

	public void remove_component(int entity, Class componentType){
		componentStores.get(componentType).remove(entity);
	}

	public <T extends Component> List<T> getAllComponentsOfType( Class<T> componentType )
	{
		HashMap<Integer, Component> store = componentStores.get( componentType );

		if( store == null )
		{
			return new LinkedList<T>();
		}
		else
		{
			return new ArrayList<T>((Collection<T>)store.values());
		}
	}

	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponent(Class<T> componentType )
	{
		HashMap<Integer, Component> store = componentStores.get( componentType );

		if( store == null)
			return new HashSet<>();

		return store.keySet();
	}

	public <T extends Component> void addComponent( int entity, T component )
	{
		HashMap<Integer, Component> store = componentStores.get( component.getClass() );

		if( store == null )
		{
			store = new HashMap<>();
			componentStores.put(component.getClass(), store );
		}

		((HashMap<Integer, T>)store).put(entity, component);
	}

	public int createEntity()
	{

		int newID = generateNewEntityID();

		if( newID < 0 )
		{
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
			for( HashMap<Integer, Component> store : componentStores.values() )
			{
				store.remove(entity);
			}
		}
	}

	private int generateNewEntityID()
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

	public Integer getPlayer() {
		return player;
	}
}
