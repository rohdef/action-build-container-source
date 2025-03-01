# Action for building containers

See [source](https://github.com/rohdef/action-build-container-source) for source code

## Annotations

OCI recommends a set of [default annotation](https://github.com/opencontainers/image-spec/blob/main/annotations.md)
where this action inserts reasonable default values when possible.
Please make sure to set the rest/customize what is needed for your needs.

```yaml
with:
  annotations: |
            org.opencontainers.image.authors=Fiktivus Maximus (maximus@dummy-email)
            org.opencontainers.image.url=https://project-domain.com
            org.opencontainers.image.documentation=https://project-domain.com/documentation
            org.opencontainers.image.vendor=Super Organization Inc
            org.opencontainers.image.ref.name=3.1-super-project
            org.opencontainers.image.title=The Super Prject
            org.opencontainers.image.description=This project does super things
```

| label                                  | description                                  | default value                         | should be set |
|----------------------------------------|----------------------------------------------|---------------------------------------|---------------|
| org.opencontainers.image.created       | Build date in full ISO8601                   | Current                               | No            |
| org.opencontainers.image.authors       | People or organization responsible for image | $GITHUB_ACTOR                         | Yes           |
| org.opencontainers.image.url           | URL for information about image              | $GITHUB_SERVER_URL/$GITHUB_REPOSITORY | Yes           |
| org.opencontainers.image.documentation | Image documentation                          | $GITHUB_SERVER_URL/$GITHUB_REPOSITORY | Yes           |
| org.opencontainers.image.source        | Image source                                 | $GITHUB_SERVER_URL/$GITHUB_REPOSITORY | No            |
| org.opencontainers.image.version       | Image version                                |                                       |               |
| org.opencontainers.image.revision      | Source control revision identifier           | $GITHUB_SHA                           | No            |
| org.opencontainers.image.vendor        | Distributing entity                          | $GITHUB_ACTOR                         | Yes           |
| org.opencontainers.image.licenses      | License                                      |                                       | Yes           |
| org.opencontainers.image.ref.name      | Name of the reference for a target           |                                       | Yes           |
| org.opencontainers.image.title         | Human-readable title                         |                                       | Yes           |
| org.opencontainers.image.description   | Human-readable description                   |                                       | Yes           |


## Labels