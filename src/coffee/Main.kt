package coffee

import java.util.Scanner

import coffee.distrib.Status
import coffee.distrib.Recipe
import coffee.distrib.DistribUI


fun main() {
	val scanner = Scanner(System.`in`)
	val passiveAction = listOf(Status.HOME, Status.BUY, Status.FILL, Status.REMAINING, Status.TAKE)
	val dist = DistribUI(550, 400, 540, 120, 9)
	dist.products.add(Recipe("espresso", 250, 0, 16, 4))
	dist.products.add(Recipe("latte", 350, 75, 20, 7))
	dist.products.add(Recipe("cappuccino",  200, 100, 12, 6))

	while (dist.status != Status.EXIT){
		var userInput = ""
		if (dist.status !in passiveAction)
			userInput = scanner.nextLine()

		dist.execute(userInput)
		if (dist.status == Status.HOME)
			println()
	}
}
