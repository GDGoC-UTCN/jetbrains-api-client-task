#!/usr/bin/env python3
import secrets

# =========================
# CONFIGURE HERE
# =========================

TEXT = "Post item"
KEY = secrets.randbelow(256)  # 0–255

# =========================


def xor_encode(text: str, key: int):
    return [ord(c) ^ key for c in text]


def main():
    encoded = xor_encode(TEXT, KEY)

    print("Original:")
    print(TEXT)

    print("\nKey:")
    print(KEY)

    print("\nEncoded (decimal):")
    print(", ".join(map(str, encoded)))

    print("\nEncoded (hex):")
    print(", ".join(hex(n) for n in encoded))

    print("\n--- Kotlin Full Declaration ---\n")

    print(f"val key = 0x{KEY:02X}")
    print("val data = intArrayOf(" + ", ".join(map(str, encoded)) + ")")
    print()
    print("val text = data")
    print("    .map { it xor key }")
    print("    .map { it.toChar() }")
    print("    .joinToString(\"\")")

    print("\n------------------------------\n")


if __name__ == "__main__":
    main()