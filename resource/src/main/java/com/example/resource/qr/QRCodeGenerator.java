package com.example.resource.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

public final class QRCodeGenerator {

    private static final int DEFAULT_SIZE = 400; // pixels
    private static final int DEFAULT_MARGIN = 1; // modules
    private static final String DEFAULT_FG = "#1835f2";
    private static final String DEFAULT_BG = "#FFFFFF";

    private QRCodeGenerator() {}

    // Public API: Base64 text (now returns Base64 of SVG)
    public static String generateQRCodeAsText(String url, String logoPath)
            throws WriterException, IOException {
        String svg = generateQRCodeSvg(url, DEFAULT_SIZE, DEFAULT_MARGIN, DEFAULT_FG, DEFAULT_BG, resolveLogoHref(logoPath), 80, true);
        return Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
    }

    // Public API: Data URL
    public static String generateQRCodeAsDataURL(String url, String logoPath)
            throws WriterException, IOException {
        String base64 = generateQRCodeAsText(url, logoPath);
        return "data:image/svg+xml;base64," + base64;
    }


    // Core SVG builder: no java.awt
    public static String generateQRCodeSvg(
            String url,
            Integer pixelSize,
            Integer marginModules,
            String fgColor,
            String bgColor,
            String logoHref,
            Integer logoPixelSize,
            boolean drawWhiteBgBehindLogo
    ) throws WriterException {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL must not be null or blank");
        }
        int size = pixelSize != null ? pixelSize : DEFAULT_SIZE;
        int margin = marginModules != null ? Math.max(0, marginModules) : DEFAULT_MARGIN;
        String fg = fgColor != null ? fgColor : DEFAULT_FG;
        String bg = bgColor != null ? bgColor : DEFAULT_BG;
        int logoSizePx = logoPixelSize != null ? logoPixelSize : Math.max(40, size / 5); // ~20% of size

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, margin);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, 0, 0, hints);

        int modules = matrix.getWidth();
        int total = modules + 2 * margin; // for viewBox

        StringBuilder sb = new StringBuilder(8 * modules * modules);
        sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"")
          .append(size).append("\" height=\"")
          .append(size).append("\" viewBox=\"0 0 ")
          .append(total).append(' ').append(total).append("\" shape-rendering=\"crispEdges\">\n");

        // Background
        sb.append("  <rect width=\"").append(total).append("\" height=\"")
          .append(total).append("\" fill=\"").append(bg).append("\"/>\n");

        // Modules (dark squares)
        sb.append("  <g fill=\"").append(fg).append("\">\n");
        for (int y = 0; y < modules; y++) {
            for (int x = 0; x < modules; x++) {
                if (matrix.get(x, y)) {
                    int sx = x + margin;
                    int sy = y + margin;
                    sb.append("    <rect x=\"").append(sx).append("\" y=\"").append(sy).append("\" width=\"1\" height=\"1\"/>\n");
                }
            }
        }
        sb.append("  </g>\n");

        // Logo overlay (optional) centered
        if (logoHref != null && !logoHref.isBlank()) {
            double center = total / 2.0;
            double logoW = logoSizePx * (total / (double) size); // convert px to module units (via viewBox)
            // square
            double logoX = center - logoW / 2.0;
            double logoY = center - logoW / 2.0;

            if (drawWhiteBgBehindLogo) {
                double pad = 0.5 * (total / (double) size) * 6; // similar to padding
                double rx = 1.2 * (total / (double) size) * 6; // rounded corners
                sb.append("  <rect x=\"").append(format(logoX - pad)).append("\" y=\"")
                  .append(format(logoY - pad)).append("\" width=\"")
                  .append(format(logoW + 2 * pad)).append("\" height=\"")
                  .append(format(logoW + 2 * pad)).append("\" rx=\"")
                  .append(format(rx)).append("\" ry=\"")
                  .append(format(rx)).append("\" fill=\"#FFFFFF\"/>\n");
            }
            sb.append("  <image x=\"").append(format(logoX)).append("\" y=\"")
              .append(format(logoY)).append("\" width=\"")
              .append(format(logoW)).append("\" height=\"")
              .append(format(logoW)).append("\" preserveAspectRatio=\"xMidYMid meet\" href=\"")
              .append(escapeAttr(logoHref)).append("\"/>\n");
        }

        sb.append("</svg>\n");
        return sb.toString();
    }

    // Resolve logoPath into an SVG <image href>: http(s) URL or data URI for classpath/file
    private static String resolveLogoHref(String logoPath) throws IOException {
        if (logoPath == null || logoPath.isBlank()) return null;
        if (logoPath.startsWith("http://") || logoPath.startsWith("https://") || logoPath.startsWith("data:")) {
            return logoPath;
        }
        if (logoPath.startsWith("classpath:")) {
            String cp = logoPath.substring("classpath:".length());
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cp)) {
                if (is == null) return null;
                byte[] bytes = is.readAllBytes();
                String b64 = Base64.getEncoder().encodeToString(bytes);
                String mime = guessImageMimeFromName(cp);
                return "data:" + mime + ";base64," + b64;
            }
        }
        // Treat as file path
        Path p = Path.of(logoPath);
        if (Files.exists(p)) {
            byte[] bytes = Files.readAllBytes(p);
            String b64 = Base64.getEncoder().encodeToString(bytes);
            String mime = guessImageMimeFromName(p.getFileName().toString());
            return "data:" + mime + ";base64," + b64;
        }
        // Fallback: maybe it's a URI string
        try {
            URI uri = URI.create(logoPath);
            if (uri.isAbsolute()) {
                return logoPath;
            }
        } catch (Exception _) {
            //Do nothing
        }
        return null;
    }

    private static String guessImageMimeFromName(String name) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }

    private static String escapeAttr(String s) {
        return s.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String format(double d) {
        // Trim trailing zeros for compactness
        String s = String.format(java.util.Locale.ROOT, "%.4f", d);
        int i = s.length() - 1;
        while (i > 0 && s.charAt(i) == '0') i--;
        if (i > 0 && s.charAt(i) == '.') i--;
        return s.substring(0, i + 1);
    }
}
