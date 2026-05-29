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
        
        let rootVC = AppLauncher.shared.create()
        window.rootViewController = rootVC
        self.window = window
        window.makeKeyAndVisible()
    }
}
