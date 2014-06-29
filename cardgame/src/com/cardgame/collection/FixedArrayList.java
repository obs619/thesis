/*
 ********************************************************************************
 * Copyright (c) 2013 Samsung Electronics, Inc.
 * All rights reserved.
 *
 * This software is a confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung Electronics.
 ********************************************************************************
 */
package com.cardgame.collection;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Fixed size array list which can hold only the specified number of elements. Adding new element to a full list will
 * remove elements from the beginning.
 * 
 * @param <E>
 *            type of the elements
 */
public class FixedArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 20130509L;

	private final int mMaximumSize;

	private FixedArrayList(int maximumSize) {
		super(maximumSize);
		mMaximumSize = maximumSize;
	}

	private FixedArrayList(Collection<? extends E> collection, int maximumSize) {
		super(collection);
		ensureCapacity(maximumSize);
		mMaximumSize = maximumSize;
	}

	/**
	 * Constructs a fixed size array list.
	 * 
	 * @param maximumSize
	 *            the maximum size to which this list will be expanded
	 * @return {@link FixedArrayList} instance
	 */
	public static <E> FixedArrayList<E> newFixedArrayList(int maximumSize) {
		checkArgument(maximumSize >= 1, "The size must be greater or equal 1 but %s provided", maximumSize);
		return new FixedArrayList<E>(maximumSize);
	}

	/**
	 * Constructs a new instance of {@link FixedArrayList} containing the elements of the specified collection.
	 * 
	 * @param maximumSize
	 *            the maximum size to which this list will be expanded
	 * @param collection
	 *            the collection of elements to add
	 * @throws {@link IllegalArgumentException} thrown when the collection's size is bigger than the {@code maximumSize}
	 *         value
	 * @return {@link FixedArrayList} instance
	 */
	public static <E> FixedArrayList<E> newFixedArrayList(Collection<? extends E> collection, int maximumSize) {
		checkArgument(maximumSize >= 1, "The size must be greater or equal 1 but %s provided", maximumSize);
		checkArgument(maximumSize >= collection.size(),
				"The size of the collection must be less or equal to maximum size. max:%s collection:%s", maximumSize,
				collection.size());
		return new FixedArrayList<E>(collection, maximumSize);
	}

	/**
	 * Adds the specified object at the end of this {@link FixedArrayList}. Removes the first element if necessary.
	 * 
	 * @param object
	 *            the object to add
	 */
	@Override
	public boolean add(E object) {
		if (size() >= mMaximumSize) {
			remove(0);
		}
		return super.add(object);
	};

	/**
	 * Adds the specified object at the given index of this {@link FixedArrayList}. Removes the first element if
	 * necessary.
	 * 
	 * @param object
	 *            the object to add
	 * @param index
	 *            the index at which to insert the object
	 */
	@Override
	public void add(int index, E object) {
		if (size() >= mMaximumSize) {
			remove(0);
		}
		super.add(index, object);
	};

	/**
	 * Adds the objects in the specified collection to this {@link FixedArrayList}. Removes elements from beginning if
	 * necessary.
	 * 
	 * @param collection
	 *            the collection of objects
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		int removeCount = size() + collection.size() - mMaximumSize;
		removeCount = removeCount > size() ? size() : removeCount;
		if (removeCount > 0) {
			removeRange(0, removeCount);
		}
		return super.addAll(collection);
	}

	/**
	 * Inserts the objects in the specified collection at the specified location in this List. The objects are added in
	 * the order they are returned from the collection's iterator. Removes elements from beginning if necessary.
	 * 
	 * @param index
	 *            the index at which to insert
	 * @param collection
	 *            the collection of objects
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		int removeCount = size() + collection.size() - mMaximumSize;
		removeCount = removeCount > size() ? size() : removeCount;
		if (removeCount > 0) {
			removeRange(0, removeCount);
		}
		return super.addAll(index, collection);
	}

	/**
	 * Ensures that after this operation the ArrayList can hold the specified number of elements without further
	 * growing. If the value is larger that the {@link FixedArrayList#mMaximumSize} it will not have any effect.
	 * 
	 * @param minimumCapacity
	 *            the minimum capacity asked for
	 */
	@Override
	public void ensureCapacity(int minimumCapacity) {
		super.ensureCapacity(minimumCapacity > mMaximumSize ? mMaximumSize : minimumCapacity);
	}

}
