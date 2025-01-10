package cn.ghostcloud.cloud.starter.utils;

import org.springframework.asm.ClassReader;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.MetadataReader;

import javax.lang.model.element.AnnotationValueVisitor;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author liwenhao
 * @since 2022/12/26 下午1:49
 */
public class SimpleMetadataReader implements MetadataReader{

    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource, ClassLoader classLoader) throws Exception {
        ClassReader classReader = getClassReader(resource);

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.classMetadata = visitor;
        this.annotationMetadata = visitor;
        this.resource = resource;

    }

    private static ClassReader getClassReader(Resource resource) throws IOException {
        try (InputStream is = new BufferedInputStream(resource.getInputStream())) {
            return new ClassReader(is);
        } catch (IllegalArgumentException ex) {
            throw new NestedIOException("ASM ClassReader failed to parse class file - " +
                    "probably due to a new Java class file version that isn't supported yet: " + resource, ex);
        }
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }
}
