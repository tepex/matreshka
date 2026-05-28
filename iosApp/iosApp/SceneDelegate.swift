import UIKit
import Habit

// ХАК ДЛЯ ОБХОДА ТРЕБОВАНИЙ COMPOSE MULTIPLATFORM НА XCODE 14:
@objc(UITextLoupeSession)
class UITextLoupeSessionDummy: NSObject {}

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?
    
    func scene(
        _ scene: UIScene,
        willConnectTo session: UISceneSession,
        options connectionOptions: UIScene.ConnectionOptions
    ) {
        // 1. Проверяем и инициализируем окно приложения
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let window = UIWindow(windowScene: windowScene)
        window.frame = UIScreen.main.bounds
        
        // 2. Создаем системный контейнер табов (Bottom Navigation)
        let tabBarController = UITabBarController()
        
        // 3. Вызываем Compose Multiplatform экран из модуля Habit
        let habitVC = HabitMainKt.HabitViewController {
            
        }
        habitVC.tabBarItem = UITabBarItem(title: "Habit", image: nil, tag: 0)
        
        // 4. Вызываем Compose Multiplatform экран из модуля Opt
        let optVC = OptMainKt.OptViewController()
        optVC.tabBarItem = UITabBarItem(title: "Opt", image: nil, tag: 1)
        
        // 5. Помещаем оба экрана в табы
        tabBarController.viewControllers = [habitVC, optVC]
        
        // 6. Делаем таб-бар корневым и отображаем окно
        //window.rootViewController = tabBarController
        
        // Проверяем реальное сохранение на устройстве через Kotlin
        if HabitMainKt.isUserLoggedIn() {
            print("Пользователь уже авторизован. Открываем табы.")
            window.rootViewController = tabBarController
        } else {
            print("Пользователь не авторизован. Показываем Login.")
            
            // Создаем LoginViewController и передаем логику переключения на табы при успехе
            let loginVC = HabitMainKt.HabitViewController() {
                DispatchQueue.main.async {
                    window.rootViewController = tabBarController
                }
            }
            window.rootViewController = loginVC
        }

        self.window = window
        window.makeKeyAndVisible()
    }
}
