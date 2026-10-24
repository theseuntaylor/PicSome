# PicSome

- A mobile application that fetches 100 images from [the Picsum's API](https://picsum.photos/).

## Tech Stack used 
- MVVM
- Dagger Hilt
- Jetpack Compose
- Retrofit
- Room Database

## Screenshots

![Home Screen](https://github.com/theseuntaylor/StoreLabs-Assessment/blob/master/app/Screenshot_20230721-145540.png)
![Favourites Screen](https://github.com/theseuntaylor/StoreLabs-Assessment/blob/master/app/Screenshot_20230721-145549.png)

## Releasing

Pushing a version tag on master's latest commit builds a signed bundle and publishes it to the
Play Store production track ([workflow](.github/workflows/deploy-playstore.yml)):

```sh
git tag v1.1.0 && git push origin v1.1.0
```

The tag sets `versionName` (`v1.1.0` → `1.1.0`), and `versionCode` is bumped by one and committed
back to master. Pull afterwards before making new commits.

The workflow needs these repository secrets:

| Secret | Value |
| --- | --- |
| `KEYSTORE_BASE64` | The upload keystore, base64-encoded (`base64 -i upload.jks`) |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias |
| `KEY_PASSWORD` | Key password |
| `SERVICE_ACCOUNT_JSON` | JSON key for a Google Cloud service account with release access in Play Console |

## Privacy

See the [privacy policy](PRIVACY_POLICY.md).
