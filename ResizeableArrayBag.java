/**
@author Daniel Santamaria
@author Hope Markley
CS2400
2/22/21 
 */
import java.util.Arrays;

public class ResizeableArrayBag<T> implements BagInterface<T>
{
    private T[] bag;
    private static final int DEFUALT_CAPACITY = 75;
    private int numberOfEntries;

    private boolean integrityOK;
    private int MAX_CAPACITY = 30;

    /** Initializes the ArrayBag with the Defualt capacity and the Integrity is set to true. */
    public ResizeableArrayBag() 
    {
        numberOfEntries = 0;
        @SuppressWarnings("unchecked")
        T[] tempBag = (T[])new Object[DEFUALT_CAPACITY];
        bag = tempBag;
        integrityOK = true;
    }
    
    /** Initializes the Arraybag with the desired capacity and the Integrity is set to true. 
     * 
        @param desiredCapacity  Desired capacity must be less than 75.
        @throws IllegalStateException throws exception if the constructor 
        attempts to create a bag with a whose capacity exceeds allowed maximum.*/
    public ResizeableArrayBag(int desiredCapacity)
    {
        if(desiredCapacity <= MAX_CAPACITY)
        {
            numberOfEntries = 0;
            @SuppressWarnings("unchecked")
            T[] tempBag = (T[])new Object[desiredCapacity];
            bag = tempBag;
            integrityOK = true;
        }
        else
            throw new IllegalStateException("Attempt to create a bag"
            + "whose capacity exceeds allowed maximum.");
    }

    public void checkIntegrity()
    {
        if(!integrityOK)
            throw new SecurityException("Array object is corrupt.");
    }
    
    public int getCurrentSize()
    {
        return numberOfEntries;
    }
    
    public boolean isEmpty()
	{
        return numberOfEntries==0;
    }
    
    /** Returns true if the ArrayBag is full.
     * 
        @return numberOfEntries == bag.length */
    public boolean isFull()
    {
        return numberOfEntries == bag.length;
    }

    public boolean add(T newEntry)
	{
        checkIntegrity();
        boolean result = true;
        if (isFull())
        {
            doubleCapacity();
        }
        else
        {
            bag[numberOfEntries] = newEntry;
            numberOfEntries++;
        }
        return result;
    }

    private void checkCapacity(int capacity)
    {
        if(capacity > MAX_CAPACITY)
            throw new IllegalStateException("Attempt to create a bag"
            + "whose capacity exceeds allowed"
            + " maximum of "+ MAX_CAPACITY);
    }

    private void doubleCapacity()
    {
        int newLength = 2 * bag.length;
        checkCapacity(newLength);
        bag = Arrays.copyOf(bag, newLength);
    }

    public T remove()
	{
        checkIntegrity();
        T result = removeEntry(numberOfEntries-1);
        return result;
    }

    public boolean remove(T anEntry)
    {
        checkIntegrity();
        int index = getIndexOf(anEntry);
        T result = removeEntry(index);

        return anEntry.equals(result);
    }

    private int getIndexOf(T anEntry)
    {
        int where =-1;
        boolean found = false;
        int index = 0;

        while(!found && (index < numberOfEntries))
        {
            if(anEntry.equals(bag[index]))
            {
                found = true;
                where = index;
            }
            index++;
        }
        return where;
    }

    private T removeEntry(int givenIndex) 
    {
        T result = null;
        
        if(!isEmpty() && (givenIndex >= 0))
        {
            result = bag[givenIndex];
            bag[givenIndex] = bag[numberOfEntries-1];
            bag[numberOfEntries-1] = null;
            numberOfEntries--;  
        }

        return result;
    }

    public void clear()
	{
        while(!isEmpty())
            remove();
    }

    public int getFrequencyOf(T anEntry)
	{
        checkIntegrity();
        int counter = 0;

        for(int index = 0; index < numberOfEntries; index++)
        {
            if(anEntry.equals(bag[index]))
            {
                counter++;
            }
        }
        return counter;
    }

    public boolean contains(T anEntry)
	{
        checkIntegrity();
        return getIndexOf(anEntry) > -1;

    }

    public T[] toArray()
    {
        @SuppressWarnings("unchecked")
        T[] result = (T[])new Object[numberOfEntries];
        for(int index = 0; index< numberOfEntries; index++)
        {
            result[index] = bag[index];
        }
        return result;
    }

    public T bagContent(int index)
    {
        @SuppressWarnings("unchecked")
        T[] copyBag = (T[])new Object[numberOfEntries];
        copyBag = bag;
        
        return copyBag[index];

    }

    public BagInterface<T> union(BagInterface<T> bag2)
    {
        checkIntegrity();
        BagInterface<T> eveything = new ResizeableArrayBag<T>();

        for (int index = 0; index < this.getCurrentSize(); index++) 
        {
            eveything.add(bag [index]);
        }
        for (int index = 0; index < bag2.getCurrentSize(); index++) 
        {
            eveything.add(bag2.bagContent(index));
        }
        return eveything;
    } 

    public BagInterface<T> intersection(BagInterface<T> bag2)
    {
        checkIntegrity();
        BagInterface<T> commonItems = new ResizeableArrayBag<>();
        
        if(this.getCurrentSize() >= bag2.getCurrentSize())
        {
            for (int index = 0; index < this.getCurrentSize(); index++) 
            {
                if((bag2.getFrequencyOf(this.bagContent(index)) > 1))
                    commonItems.add(this.bagContent(index));    
            }
        }
        else
        {
            for (int index = 0; index < bag2.getCurrentSize(); index++) 
            {
                if(this.getFrequencyOf(bag2.bagContent(index)) > 1)    
                    commonItems.add(bag2.bagContent(index));
            } 
        }
        
        return commonItems;
    }

    public BagInterface<T> difference(BagInterface<T> bag2)
    {
        checkIntegrity();
        BagInterface<T> leftOver = new ResizeableArrayBag<>();

        for (int index = 0; index < this.getCurrentSize(); index++) 
        {
            if(bag2.getFrequencyOf(this.bagContent(index)) < this.getFrequencyOf(this.bagContent(index)))
            {
                leftOver.add(this.bagContent(index));    
            }    
        }
        return leftOver;
    }
    
}
