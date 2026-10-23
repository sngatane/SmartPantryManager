# Recipe Matching

The intended strict-matching rule is that a recipe is suggested only when all required ingredients and quantities are available.

A matching process compares recipe requirements with pantry quantities. A missing ingredient or insufficient quantity should cause the recipe to be rejected.

Ingredient-name normalisation can handle simple spelling variations, but it does not guarantee that synonyms or different measurement units are equivalent.

Check the implementation in DatabaseHelper.java before claiming every case is supported.

