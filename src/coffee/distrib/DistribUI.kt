package coffee.distrib

import coffee.distrib.Status
import coffee.distrib.Recipe


open class DistribUI(
	var money: Int, 
	var water: Int,
	var milk: Int,
	var coffee: Int,
	var cup: Int){
		var products: MutableList<Recipe> = mutableListOf()
		var status = Status.HOME


	fun isAvailable(product: Int, quantity: Int = 1): Boolean {
		var missing = "nothing"
		var isAvailable = false
		val recipe = this.products[product]

		if (this.water < quantity * recipe.water)
			missing = "water"
		else if (this.milk < quantity * recipe.milk)
			missing = "milk"
		else if (this.coffee < quantity * recipe.coffee)
			missing = "coffee"
		else if (this.cup < quantity)
			missing = "cup"
		else
			isAvailable = true
		if (!isAvailable)
			println("Sorry, not enough $missing!")
		else
			println("I have enough resources, making you a coffee!")
		return isAvailable
	}

	fun home(userInput: String){
		when (userInput) {
			"buy" -> this.status = Status.BUY
			"fill" -> this.status = Status.FILL
			"take" -> this.status = Status.TAKE
			"remaining" -> this.status = Status.REMAINING
			else -> this.status = Status.EXIT
		}
	}

	fun homePrompt() {
		print("Write action (buy, fill, take, remaining, exit): ")
		this.status = Status.HOME_SELECTION
	}

	fun buy(userInput: String, quantity: Int = 1){
		if (userInput != "back"){
			val choice: Int? = userInput.toIntOrNull()
			if(choice is Int && choice - 1 in this.products.indices){
				val recipe = this.products[choice - 1]
				if(this.isAvailable(choice - 1, quantity)){
					this.water = this.water - (recipe.water * quantity)
					this.milk = this.milk - (recipe.milk * quantity)
					this.coffee = this.coffee - (recipe.coffee * quantity)
					this.cup = this.cup - quantity
					this.money = this.money + recipe.price
				}
			}
		} 
	}

	fun buyPrompt() {
		print("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
		this.status = Status.BUY_SELECTION
	}

	fun fill(userInput: String){
		val quantity: Int? = userInput.toIntOrNull()
		if (quantity is Int && quantity > 0){
			when (this.status){
				Status.FILL_WATER -> this.water += quantity
				Status.FILL_MILK -> this.milk += quantity
				Status.FILL_COFFEE -> this.coffee += quantity
				Status.FILL_CUP -> this.cup += quantity
				else -> {
					println("-Error: unknown status")
					this.status = Status.HOME
				}
			}
		}
	}

	fun fillWaterPrompt() {
		print("\nWrite how many ml of water do you want to add ")
		this.status = Status.FILL_WATER
	}

	fun fillMilkPrompt() {
		print("Write how many ml of milk do you want to add ")
		this.status = Status.FILL_MILK
	}

	fun fillCoffeePrompt() {
		print("Write how many grams of coffee beans do you want to add ")
		this.status = Status.FILL_COFFEE
	}

	fun fillCupPrompt() {
		print("Write how many disposable cups of coffee do you want to add ")
		this.status = Status.FILL_CUP
	}

	fun take(){
		val total = this.money
		this.money = 0
		println("\nI gave you $$total")
	}

	fun remaining(){
		println("\nThe coffee machine has:")
		println("${this.water} of water")
		println("${this.milk} of milk")
		println("${this.coffee} of coffee beans")
		println("${this.cup} of disposable cups")
		println("${this.money} of money")
	}

	fun execute(userInput: String){
		when (this.status) {
			Status.HOME -> this.homePrompt()
			Status.HOME_SELECTION -> this.home(userInput)
			Status.BUY -> this.buyPrompt()
			Status.BUY_SELECTION -> {
				this.buy(userInput)
				this.status = Status.HOME
			}
			Status.FILL -> this.fillWaterPrompt()
			Status.FILL_WATER -> {
				this.fill(userInput)
				this.fillMilkPrompt()
			}
			Status.FILL_MILK -> {
				this.fill(userInput)
				this.fillCoffeePrompt()
			}
			Status.FILL_COFFEE -> {
				this.fill(userInput)
				this.fillCupPrompt()
			}
			Status.FILL_CUP -> {
				this.fill(userInput)
				this.status = Status.HOME
			}
			Status.TAKE -> {
				this.take()
				this.status = Status.HOME
			}
			Status.REMAINING -> {
				this.remaining()
				this.status = Status.HOME
			}
		}
	}
}
