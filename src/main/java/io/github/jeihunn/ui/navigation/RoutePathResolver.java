package io.github.jeihunn.ui.navigation;

import java.net.URI;
import java.util.Objects;

/** Resolves and normalizes URL paths for route comparison. */
public final class RoutePathResolver {

  private RoutePathResolver() {}

  /** Extracts a normalized path from a URL, ignoring query and fragment parts. */
  public static String extractNormalizedPath(String url) {
    String rawUrl = requireNonBlank(url, "URL");
    URI uri = parseUri(rawUrl, "URL");
    return normalizePath(uri.getPath());
  }

  /**
   * Resolves expected route path from base URL and route value. Blank route path maps to base path.
   * Absolute route URL uses its own path. Route path starting with "/" is treated as an absolute
   * app path. Other values are resolved relative to base URL.
   */
  public static String resolveExpectedPath(String baseUrl, String routePath) {
    String rawBaseUrl = requireNonBlank(baseUrl, "Base URL");
    URI baseUri = parseUri(rawBaseUrl, "Base URL");
    String basePath = normalizePath(baseUri.getPath());

    if (routePath == null || routePath.isBlank()) {
      return basePath;
    }

    String candidate = routePath.trim();

    if (isAbsoluteUrl(candidate)) {
      return extractNormalizedPath(candidate);
    }

    if (candidate.startsWith("/")) {
      return normalizePath(candidate);
    }

    URI resolved = baseUri.resolve(candidate);
    return normalizePath(resolved.getPath());
  }

  /** Returns whether two paths are equal after normalization. */
  public static boolean areSamePath(String leftPath, String rightPath) {
    return normalizePath(leftPath).equals(normalizePath(rightPath));
  }

  /** Normalizes path format to provide stable comparisons. */
  static String normalizePath(String path) {
    if (path == null || path.isBlank()) {
      return "/";
    }

    String normalized = path.trim().replace('\\', '/');
    normalized = normalized.replaceAll("/+", "/");
    if (!normalized.startsWith("/")) {
      normalized = "/" + normalized;
    }

    if (normalized.length() > 1 && normalized.endsWith("/")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    return normalized;
  }

  private static boolean isAbsoluteUrl(String value) {
    try {
      URI uri = URI.create(value);
      return uri.isAbsolute() && uri.getScheme() != null;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private static URI parseUri(String value, String fieldName) {
    try {
      return URI.create(value);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(fieldName + " is not a valid URI: " + value, e);
    }
  }

  private static String requireNonBlank(String value, String fieldName) {
    Objects.requireNonNull(value, fieldName + " cannot be null");
    if (value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " cannot be blank");
    }
    return value;
  }
}
