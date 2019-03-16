/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing authorizes and
 * limitations under the License.
 */

/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
package com.honvay.hdms.framework.core;

import com.honvay.hdms.framework.core.exception.ServiceException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Assertion utility class that assists in validating arguments.
 *
 * <p>Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>For example, if the contract of a public method states it does not
 * allow {@code null} arguments, {@code ServiceAssert} can be used to validate that
 * contract. Doing this clearly indicates a contract violation when it
 * occurs and protects the class's invariants.
 *
 * <p>Typically used to validate method arguments rather than config
 * properties, to check for cases that are usually programmer errors rather
 * than config errors. In contrast to config initialization
 * credential, there is usually no point in falling back to defaults in such methods.
 *
 * <p>This class is similar to JUnit's assertion library. If an argument value is
 * deemed invalid, an {@link IllegalArgumentException} is thrown (typically).
 * For example:
 *
 * <pre class="credential">
 * ServiceAssert.notNull(clazz, "The class must not be null");
 * ServiceAssert.isTrue(i > 0, "The value must be greater than zero");</pre>
 *
 * <p>Mainly for internal use within the framework; consider
 * <a href="http://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a>
 * for a more comprehensive suite of {@code String} utilities.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Colin Sampaleanu
 * @author Rob Harrop
 * @since 1.1.2
 */
public abstract class ServiceAssert {

	/**
	 * ServiceAssert a boolean expression, throwing an {@code IllegalArgumentException}
	 * if the expression evaluates to {@code false}.
	 * <pre class="credential">ServiceAssert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 *
	 * @param expression a boolean expression
	 * @param message    the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if {@code expression} is {@code false}
	 */
	public static void isTrue(boolean expression, ErrorMessage message) {
		if (!expression) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert a boolean expression, throwing an {@code IllegalArgumentException}
	 * if the expression evaluates to {@code false}.
	 * <pre class="credential">
	 * ServiceAssert.isTrue(i &gt; 0, () -&gt; "The value '" + i + "' must be greater than zero");
	 * </pre>
	 *
	 * @param expression      a boolean expression
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if {@code expression} is {@code false}
	 * @since 5.0
	 */
	public static void isTrue(boolean expression, Supplier<ErrorMessage> messageSupplier) {
		if (!expression) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}


	/**
	 * ServiceAssert that an object is {@code null}.
	 * <pre class="credential">ServiceAssert.isNull(value, "The value must be null");</pre>
	 *
	 * @param object  the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(@Nullable Object object, ErrorMessage message) {
		if (object != null) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that an object is {@code null}.
	 * <pre class="credential">
	 * ServiceAssert.isNull(value, () -&gt; "The value '" + value + "' must be null");
	 * </pre>
	 *
	 * @param object          the object to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the object is not {@code null}
	 * @since 5.0
	 */
	public static void isNull(@Nullable Object object, Supplier<ErrorMessage> messageSupplier) {
		if (object != null) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that an object is not {@code null}.
	 * <pre class="credential">ServiceAssert.notNull(clazz, "The class must not be null");</pre>
	 *
	 * @param object  the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void notNull(@Nullable Object object, ErrorMessage message) {
		if (object == null) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that an object is not {@code null}.
	 * <pre class="credential">
	 * ServiceAssert.notNull(clazz, () -&gt; "The class '" + clazz.getName() + "' must not be null");
	 * </pre>
	 *
	 * @param object          the object to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the object is {@code null}
	 * @since 5.0
	 */
	public static void notNull(@Nullable Object object, Supplier<ErrorMessage> messageSupplier) {
		if (object == null) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="credential">ServiceAssert.hasLength(name, "Name must not be empty");</pre>
	 *
	 * @param text    the String to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the text is empty
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(@Nullable String text, ErrorMessage message) {
		if (!StringUtils.hasLength(text)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="credential">
	 * ServiceAssert.hasLength(name, () -&gt; "Name for account '" + account.getId() + "' must not be empty");
	 * </pre>
	 *
	 * @param text            the String to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the text is empty
	 * @see StringUtils#hasLength
	 * @since 5.0
	 */
	public static void hasLength(@Nullable String text, Supplier<ErrorMessage> messageSupplier) {
		if (!StringUtils.hasLength(text)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that the given String contains valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="credential">ServiceAssert.hasText(name, "'name' must not be empty");</pre>
	 *
	 * @param text    the String to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the text does not contain valid text content
	 * @see StringUtils#hasText
	 */
	public static void hasText(@Nullable String text, ErrorMessage message) {
		if (!StringUtils.hasText(text)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that the given String contains valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="credential">
	 * ServiceAssert.hasText(name, () -&gt; "Name for account '" + account.getId() + "' must not be empty");
	 * </pre>
	 *
	 * @param text            the String to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the text does not contain valid text content
	 * @see StringUtils#hasText
	 * @since 5.0
	 */
	public static void hasText(@Nullable String text, Supplier<ErrorMessage> messageSupplier) {
		if (!StringUtils.hasText(text)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that the given text does not contain the given substring.
	 * <pre class="credential">ServiceAssert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 *
	 * @param textToSearch the text to search
	 * @param substring    the substring to find within the text
	 * @param message      the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the text contains the substring
	 */
	public static void doesNotContain(@Nullable String textToSearch, String substring, ErrorMessage message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that the given text does not contain the given substring.
	 * <pre class="credential">
	 * ServiceAssert.doesNotContain(name, forbidden, () -&gt; "Name must not contain '" + forbidden + "'");
	 * </pre>
	 *
	 * @param textToSearch    the text to search
	 * @param substring       the substring to find within the text
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the text contains the substring
	 * @since 5.0
	 */
	public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<ErrorMessage> messageSupplier) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}


	/**
	 * ServiceAssert that an array contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="credential">ServiceAssert.notEmpty(array, "The array must contain elements");</pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array is {@code null} or contains no elements
	 */
	public static void notEmpty(@Nullable Object[] array, ErrorMessage message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that an array contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="credential">
	 * ServiceAssert.notEmpty(array, () -&gt; "The " + arrayType + " array must contain elements");
	 * </pre>
	 *
	 * @param array           the array to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the object array is {@code null} or contains no elements
	 * @since 5.0
	 */
	public static void notEmpty(@Nullable Object[] array, Supplier<ErrorMessage> messageSupplier) {
		if (ObjectUtils.isEmpty(array)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}


	/**
	 * ServiceAssert that an array contains no {@code null} elements.
	 * <p>Note: Does not complain if the array is empty!
	 * <pre class="credential">ServiceAssert.noNullElements(array, "The array must contain non-null elements");</pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array contains a {@code null} element
	 */
	public static void noNullElements(@Nullable Object[] array, ErrorMessage message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new ServiceException(message);
				}
			}
		}
	}

	/**
	 * ServiceAssert that an array contains no {@code null} elements.
	 * <p>Note: Does not complain if the array is empty!
	 * <pre class="credential">
	 * ServiceAssert.noNullElements(array, () -&gt; "The " + arrayType + " array must contain non-null elements");
	 * </pre>
	 *
	 * @param array           the array to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the object array contains a {@code null} element
	 * @since 5.0
	 */
	public static void noNullElements(@Nullable Object[] array, Supplier<ErrorMessage> messageSupplier) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new ServiceException(nullSafeGet(messageSupplier));
				}
			}
		}
	}

	/**
	 * ServiceAssert that a collection contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="credential">ServiceAssert.notEmpty(collection, "Collection must contain elements");</pre>
	 *
	 * @param collection the collection to check
	 * @param message    the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the collection is {@code null} or
	 *                                  contains no elements
	 */
	public static void notEmpty(@Nullable Collection<?> collection, ErrorMessage message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * ServiceAssert that a collection contains elements; that is, it must not be
	 * {@code null} and must contain at least one element.
	 * <pre class="credential">
	 * ServiceAssert.notEmpty(collection, () -&gt; "The " + collectionType + " collection must contain elements");
	 * </pre>
	 *
	 * @param collection      the collection to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the collection is {@code null} or
	 *                                  contains no elements
	 * @since 5.0
	 */
	public static void notEmpty(@Nullable Collection<?> collection, Supplier<ErrorMessage> messageSupplier) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}


	/**
	 * ServiceAssert that a Map contains entries; that is, it must not be {@code null}
	 * and must contain at least one entry.
	 * <pre class="credential">ServiceAssert.notEmpty(map, "Map must contain entries");</pre>
	 *
	 * @param map     the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the map is {@code null} or contains no entries
	 */
	public static void notEmpty(@Nullable Map<?, ?> map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * ServiceAssert that a Map contains entries; that is, it must not be {@code null}
	 * and must contain at least one entry.
	 * <pre class="credential">
	 * ServiceAssert.notEmpty(map, () -&gt; "The " + mapType + " map must contain entries");
	 * </pre>
	 *
	 * @param map             the map to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails
	 * @throws IllegalArgumentException if the map is {@code null} or contains no entries
	 * @since 5.0
	 */
	public static void notEmpty(@Nullable Map<?, ?> map, Supplier<ErrorMessage> messageSupplier) {
		if (CollectionUtils.isEmpty(map)) {
			throw new ServiceException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that a Map contains entries; that is, it must not be {@code null}
	 * and must contain at least one entry.
	 *
	 * @deprecated as of 4.3.7, in favor of {@link #notEmpty(Map, String)}
	 */
	@Deprecated
	public static void notEmpty(@Nullable Map<?, ?> map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	/**
	 * ServiceAssert that the provided object is an instance of the provided class.
	 * <pre class="credential">ServiceAssert.instanceOf(Foo.class, foo, "Foo expected");</pre>
	 *
	 * @param type    the type to check against
	 * @param obj     the object to check
	 * @param message a message which will be prepended to provide further context.
	 *                If it is empty or ends in ":" or ";" or "," or ".", a full exception message
	 *                will be appended. If it ends in a space, the name of the offending object's
	 *                type will be appended. In any other case, a ":" with a space and the name
	 *                of the offending object's type will be appended.
	 * @throws IllegalArgumentException if the object is not an instance of type
	 */
	public static void isInstanceOf(Class<?> type, @Nullable Object obj, ErrorMessage message) {
		notNull(type, ErrorConstants.ILLEGAL_ARGUMENT);
		if (!type.isInstance(obj)) {
			instanceCheckFailed(type, obj, message);
		}
	}

	/**
	 * ServiceAssert that the provided object is an instance of the provided class.
	 * <pre class="credential">
	 * ServiceAssert.instanceOf(Foo.class, foo, () -&gt; "Processing " + Foo.class.getSimpleName() + ":");
	 * </pre>
	 *
	 * @param type            the type to check against
	 * @param obj             the object to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails. See {@link #isInstanceOf(Class, Object, ErrorMessage)} for details.
	 * @throws IllegalArgumentException if the object is not an instance of type
	 * @since 5.0
	 */
	public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<ErrorMessage> messageSupplier) {
		notNull(type, ErrorConstants.of(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), "Type to check against must not be null"));
		if (!type.isInstance(obj)) {
			instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that the provided object is an instance of the provided class.
	 * <pre class="credential">ServiceAssert.instanceOf(Foo.class, foo);</pre>
	 *
	 * @param type the type to check against
	 * @param obj  the object to check
	 * @throws IllegalArgumentException if the object is not an instance of type
	 */
	public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
		isInstanceOf(type, obj, ErrorConstants.ILLEGAL_ARGUMENT);
	}

	/**
	 * ServiceAssert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <pre class="credential">ServiceAssert.isAssignable(Number.class, myClass, "Number expected");</pre>
	 *
	 * @param superType the super type to check against
	 * @param subType   the sub type to check
	 * @param message   a message which will be prepended to provide further context.
	 *                  If it is empty or ends in ":" or ";" or "," or ".", a full exception message
	 *                  will be appended. If it ends in a space, the name of the offending sub type
	 *                  will be appended. In any other case, a ":" with a space and the name of the
	 *                  offending sub type will be appended.
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, ErrorMessage message) {
		notNull(superType, ErrorConstants.of(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), "Super type to check against must not be null"));
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, message);
		}
	}

	/**
	 * ServiceAssert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <pre class="credential">
	 * ServiceAssert.isAssignable(Number.class, myClass, () -&gt; "Processing " + myAttributeName + ":");
	 * </pre>
	 *
	 * @param superType       the super type to check against
	 * @param subType         the sub type to check
	 * @param messageSupplier a supplier for the exception message to use if the
	 *                        assertion fails. See {@link #isAssignable(Class, Class, ErrorMessage)} for details.
	 * @throws IllegalArgumentException if the classes are not assignable
	 * @since 5.0
	 */
	public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<ErrorMessage> messageSupplier) {
		notNull(superType, ErrorConstants.of(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), "Super type to check against must not be null"));
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
		}
	}

	/**
	 * ServiceAssert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <pre class="credential">ServiceAssert.isAssignable(Number.class, myClass);</pre>
	 *
	 * @param superType the super type to check
	 * @param subType   the sub type to check
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, ErrorConstants.ILLEGAL_ARGUMENT);
	}


	private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable ErrorMessage msg) {
		String className = (obj != null ? obj.getClass().getName() : "null");
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg.getMessage())) {
			if (endsWithSeparator(msg.getMessage())) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg.getMessage(), className);
				defaultMessage = false;
			}
		}
		if (defaultMessage) {
			result = result + ("Object of class [" + className + "] must be an instance of " + type);
		}
		throw new IllegalArgumentException(result);
	}

	private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable ErrorMessage msg) {
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg.getMessage())) {
			if (endsWithSeparator(msg.getMessage())) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg.getMessage(), subType);
				defaultMessage = false;
			}
		}
		if (defaultMessage) {
			result = result + (subType + " is not assignable to " + superType);
		}
		throw new IllegalArgumentException(result);
	}

	private static boolean endsWithSeparator(String msg) {
		return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
	}

	private static String messageWithTypeName(String msg, @Nullable Object typeName) {
		return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
	}

	@Nullable
	private static ErrorMessage nullSafeGet(@Nullable Supplier<ErrorMessage> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

}
