package main

import (
	"encoding/base64"
	"errors"
	"fmt"
	"github.com/farmerx/gorsa"
	"log"
)

var Pubkey = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwM/gkIOl2uuiX1TU7jY+
tXw2t1rysLxXhsThJiXeTsnRcBGJJFexvyABWr99L6AfZG9tfM7tvVqtUe3mpiQt
VIeck1cxVL0okhFiTspfTmBFLBZLmahvwzdMhOwD24aD2khEJIqyRCTXXlmrk1Li
kHFOTAC9hemwCDHr74VzrPdxXm5cFbFoXggf4CnJ6+LFBnEugPIGWDjrg+UG7Liu
mbzABcZXIGD9R8OX8Ral6Wt/GrcKAXHYhTakmbN9ZY3zdCE0qnkuQiBPTCwckGln
Km8bbunFZgyElRVvgD5w9SXm8bBmC9ZA+glTu0O9gOV0itOqUYkvqanx5Hk1FImC
/wIDAQAB
-----END PUBLIC KEY-----
`

var Pirvatekey = `-----BEGIN RSA PRIVATE KEY-----
MIIEowIBAAKCAQEAwM/gkIOl2uuiX1TU7jY+tXw2t1rysLxXhsThJiXeTsnRcBGJ
JFexvyABWr99L6AfZG9tfM7tvVqtUe3mpiQtVIeck1cxVL0okhFiTspfTmBFLBZL
mahvwzdMhOwD24aD2khEJIqyRCTXXlmrk1LikHFOTAC9hemwCDHr74VzrPdxXm5c
FbFoXggf4CnJ6+LFBnEugPIGWDjrg+UG7LiumbzABcZXIGD9R8OX8Ral6Wt/GrcK
AXHYhTakmbN9ZY3zdCE0qnkuQiBPTCwckGlnKm8bbunFZgyElRVvgD5w9SXm8bBm
C9ZA+glTu0O9gOV0itOqUYkvqanx5Hk1FImC/wIDAQABAoIBAQCU6Td/hR036Zp6
0/Krk1t1INzFWUlsM9nJbQ6SPtbltLq7Od/YIJ5XhOlm49NUT+9OyzwdE1fLUVfg
mcLz96bGzQGhTcr8ribCQQjS3zsiNfnWZdW3c731YCUJ15nXFpmIoR13t2KcfHOV
G94GJxJyCgA1dLDq1qOsgJm+YCwJna3rhjKPHi+xMjx3aIiS983DLmn765BHWMFn
2TVperyocaM1PEADIM0rXEuAMtdlTX4pdc5ol/04p7PXOQ2YpupvEA2pQwMMRriC
s9NNngh4cxySrhYuJ8nIEjvma+x6sNmhXkR3CEBIjk8VVKNz10yh/vu4z9hPuO1q
yschvvbZAoGBAOlVF98ah3BUj2PD8wSHjdaeSSz5gNfcdUqOsbATWJXoj+3rVKck
jwaWrM2sw5gQDjunwo7ZJB1zydryN4QELJmQB9/3IEjm7b55/yQ7/+GpyOwZQMA5
Qs/fDgABjpSkcMZPznMJ+B59EIb2DUxOSOYVgGWy135s87l6zY8jQ2/jAoGBANOL
DesrI2uGTaObZalzPX6uLOf4aklr9A7ua4UVfdNQQEzOT9rJLCMweiqPRN56IwaW
wl6CEoPb6fcqIzsojvjScJL12pDieCGxtUCMM/WFH1LqzljvjvhZfd0iYhj8QtHf
ETR74h0VFkIeMh0ig4xiE8dvgx6dMWbw2A+gnJM1AoGAbJCQK9lvHR/rG7NHdlsP
7sT1LTzl6woDdO5R1Kgbtpxfo4a0e5RyC9G9k3EIDM+jn7QdvVNhD2cZi4rZs84x
Zj3yvA2C8wino9GlmhlkpH3YI6pYS1gZMxkIrY4L5jNg2ZS2bSY9UQpFz1XauB9Q
oeVtOWRuCb/t4uY1kJ0KupsCgYAMzt6I3pPGbttEjKQbHl8azHDrmp7U0zRzsvvP
kv2LTdCW1eb/2C5T8xtyeDozPjHgtEAQeM+mLCp+TI7Vhh8h8ZvfSgNxwkSDxb6z
qcl7lBPRcWYM/HQrlT+JpAtMd/mTu23PP7DxEdXHKwzdtrlARq/r41SNBfd3xB1Q
1OimAQKBgAudzy57OSfWG+bsOH7fiIHsZTjSs+CjEQ/Sz8bsjOlt9HxdcQW6kcga
+A9elmvWFRFIebW9AN99LciBY6zxUq5plWQub7y/vQIltlMFQTKTNIeexue8iB5P
V1662adENEr0jFexvFUEbrhEY+gGwcdwQ86NgZhyjelEVVmFZcKc
-----END RSA PRIVATE KEY-----
`

// 初始化设置公钥和私钥
func init() {
	if err := gorsa.RSA.SetPublicKey(Pubkey); err != nil {
		log.Fatalln(`set public key :`, err)
	}
	if err := gorsa.RSA.SetPrivateKey(Pirvatekey); err != nil {
		log.Fatalln(`set private key :`, err)
	}
}

func main() {
	// 公钥加密私钥解密
	if err := applyPubEPriD(); err != nil {
		log.Println(err)
	}
	// 公钥解密私钥加密
	if err := applyPriEPubD(); err != nil {
		log.Println(err)
	}

	fmt.Println("==============================")
	var encryptByPubKeyStr = "XarXXtv66ZgwKjJQSiZ520IyA0pirilkMBPJNXML2bDyujvyCj3fiQZwjfplRqDXNZncggcwrIS28k9vP/djArpQTEOoARzBl1qLwUvowq1a5hezvxI52DYVPKWgHjcIaiYn0UOdUHrbAcuZlDzBu7ohbWYMr+xESiZahAlbVrEo2L2p8Sb95eh4v5lD8MNShxIy2GCbRidbe07DZZFwF1/4Ep0BLfKZsYg6Y20Db3Psjj15Ij2VPlOaiXaocC9KSraStsr7WFbL849QR+eieESRFrQz/lSkFH//zVK9REV1+L+FYg0ofSEBd3lBTe3N9WAkEkB1XPjoXcVVPZaA9g=="
	var plain , errr  = base64.StdEncoding.DecodeString(encryptByPubKeyStr)
	if errr != nil {
		fmt.Println(errr)
	}
	var priDecrypt, err = gorsa.RSA.PriKeyDECRYPT(plain)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("私钥解密:用java语言实现的公钥加密后的密文：" + string(priDecrypt))

	var encryptByPriKeyStr = "Pgl0LU7EQt17WzTvBJvkyJ7Qcdy/Z8OX80ClnPgoR8YlOlPpgBJHKf5cuwW1Eb0dcfmUo3fOt5/NIwGeGPzK0QJUq12nyy2cQ472zno7l0N50vRhvj6bSsoR5MrDKBPZMIrOBOCaIZeRanTWYjTkIidzjK/VAa+URhcXIzRk3/cbde8K94T+/f0aSYb0s0SRjY79cABkxx/jptIoN9iBhndbITwmXJT0NPiqtEaahbqFtHT5T9OKoUoWNeI6LFnZjaYWdamw1Q8LtzXLW3Bx1t8Bg+Xcq8c/vrc9sI7TvDHKM+/A47zJunBca6TtbJmx7lOfGemYbYGziG7lPE+pfg=="
	plain , _  = base64.StdEncoding.DecodeString(encryptByPriKeyStr)
	priDecrypt, err = gorsa.RSA.PubKeyDECRYPT(plain)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("公钥解密:用java语言实现的私钥加密后的密文：" + string(priDecrypt))

}

// 公钥加密私钥解密
func applyPubEPriD() error {
	pubenctypt, err := gorsa.RSA.PubKeyENCTYPT([]byte(`hello world`))
	if err != nil {
		return err
	}
	fmt.Println("public encrypt:" + base64.StdEncoding.EncodeToString(pubenctypt))
	pridecrypt, err := gorsa.RSA.PriKeyDECRYPT(pubenctypt)
	if err != nil {
		return err
	}
	if string(pridecrypt) != `hello world` {
		return errors.New(`解密失败`)
	}
	return nil
}

// 公钥解密私钥加密
func applyPriEPubD() error {
	prienctypt, err := gorsa.RSA.PriKeyENCTYPT([]byte(`hello world`))
	if err != nil {
		return err
	}
	fmt.Println("private encrypt:" + base64.StdEncoding.EncodeToString(prienctypt))
	pubdecrypt, err := gorsa.RSA.PubKeyDECRYPT(prienctypt)
	if err != nil {
		return err
	}
	if string(pubdecrypt) != `hello world` {
		return errors.New(`解密失败`)
	}
	return nil
}
