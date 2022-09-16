package com.nil.utilities;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public abstract class KeyGeneratorUtils {

  /**
   * HMAC 암호화 해시키를 반환합니다
   * @return
   */
  public static final SecretKey generateSecretKey() {
    try {
      return KeyGenerator.getInstance("HmacSha256").generateKey();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * RSA 암호화 키 쌍을 반환합니다
   * @return
   */
  public static final KeyPair generateRsaKey() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * 타원 곡선 암호화 키 쌍을 반환합니다
   * @return
   */
  public static final KeyPair generateEcKey() {
    final var ellipticCurve = new EllipticCurve(
        new ECFieldFp(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
        new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
        new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291")
    );
    final var point = new ECPoint(
        new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
        new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109")
    );
    final var spec = new ECParameterSpec(
        ellipticCurve,
        point,
        new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
        1
    );

    try {
      final var keyPairGenerator = KeyPairGenerator.getInstance("EC");
      keyPairGenerator.initialize(spec);

      return keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

}
